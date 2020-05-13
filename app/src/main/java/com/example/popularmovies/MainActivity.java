package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.popularmovies.db.FavoriteMovie;
import com.example.popularmovies.db.FavoriteMovieViewModel;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.network.ApiHandler;
import com.example.popularmovies.network.NetworkCheck;
import com.example.popularmovies.recycleviewadapter.MovieListAdapter;
import com.example.popularmovies.recycleviewadapter.MovieItemClickListener;
import com.example.popularmovies.utils.MovieJSONUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieItemClickListener {
    private static final String TAG = "MainActivity";
    RecyclerView moviePosterRV;
    private List<Movie> mList;
    MovieListAdapter adapter;
    private final String POPULAR_MOVIE = "popular";
    private final String TOP_RATED_MOVIE = "top_rated";
    private static GetMovieJSON getMovieJSON;

    private List<FavoriteMovie> favoriteMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null){
            Log.d(TAG, "onCreate:\n "+savedInstanceState.getString("he"));
        }

        favoriteMoviesList = new ArrayList<>();

        if (NetworkCheck.isConnected(this)) {
            downloadData(POPULAR_MOVIE);
        } else {
            Log.e(TAG, "onCreate: No Internet Connection");
        }
    }

    public static class GetMovieJSON extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetMovieJSON";
        private String data = "";
        private GetMovieJSONAsyncTaskListener listener;

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: start");
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setReadTimeout(20000);
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    data = data + line;

            } catch (IOException | SecurityException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG, "doInBackground: end");
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: start");

            if (listener != null) {
                listener.onGetMovieJSONAsyncTaskFinished(s);
            }
            Log.d(TAG, "onPostExecute: end");
        }

        public void setListener(GetMovieJSONAsyncTaskListener listener) {
            this.listener = listener;
        }

        public interface GetMovieJSONAsyncTaskListener {
            void onGetMovieJSONAsyncTaskFinished(String json);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: start");
        item.setChecked(true);
        int menuItemSelectedId = item.getItemId();
        switch (menuItemSelectedId) {
            case R.id.menu_sort_by_popularity:
                setTitle(R.string.title_popular_movies);
                downloadData(POPULAR_MOVIE);
                break;
            case R.id.menu_sort_by_rating:
                setTitle(R.string.title_rated_movies);
                downloadData(TOP_RATED_MOVIE);
                break;
            case R.id.menu_sort_by_favorite:
                setTitle(R.string.title_favorite_movies);
                setupFavoriteViewModel();
        }
        Log.d(TAG, "onOptionsItemSelected: end");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(int pos, Movie movie, ImageView sharedImageView) {
        String transitionName = ViewCompat.getTransitionName(sharedImageView);
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

        Bundle b = new Bundle();
        b.putParcelable(MovieDetailActivity.EXTRA_PARSABLE_MOVIE, movie);
        b.putString(MovieDetailActivity.EXTRA_TRANSITION_NAME, transitionName);
        intent.putExtras(b);

        ActivityOptionsCompat options;
        assert transitionName != null;
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, sharedImageView, transitionName);
        startActivity(intent, options.toBundle());
    }

    private void downloadData(String sortBy) {
        Log.d(TAG, "downloadData: start");
        getMovieJSON = new GetMovieJSON();
        getMovieJSON.setListener(json -> {
            Log.d(TAG, "onGetMovieJSONAsyncTaskFinished: ");
            mList = MovieJSONUtils.parseMovie(json);

            adapter = new MovieListAdapter(mList, MainActivity.this);
            moviePosterRV = findViewById(R.id.recyclerview_movieposter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 6);
            adapter.getSpanSizeLookup(gridLayoutManager);

            moviePosterRV.setAdapter(adapter);
            moviePosterRV.setLayoutManager(gridLayoutManager);
            adapter.notifyDataSetChanged();
        });
        String url = ApiHandler.movieUri(sortBy);
        getMovieJSON.execute(url);
        Log.d(TAG, "downloadData: end");
    }

    @Override
    protected void onDestroy() {
        getMovieJSON.setListener(null);
        super.onDestroy();
    }

    private void loadFavMovies() {
       clearMoviesList();
        for (int i = 0; i < favoriteMoviesList.size(); i++) {
            Movie movie = new Movie(
                    favoriteMoviesList.get(i).getId(),
                    favoriteMoviesList.get(i).getTitle(),
                    favoriteMoviesList.get(i).getOverview(),
                    favoriteMoviesList.get(i).getPosterPath(),
                    favoriteMoviesList.get(i).getBackdropPath(),
                    Double.parseDouble(favoriteMoviesList.get(i).getPopularity()),
                    false,
                    Double.parseDouble(favoriteMoviesList.get(i).getVoteAverage()),
                    Integer.parseInt(favoriteMoviesList.get(i).getVoteCount()),
                    favoriteMoviesList.get(i).getReleaseDate());
            mList.add(movie);
        }
        adapter.setMovieList(mList);

    }
    private void clearMoviesList() {
        if (mList != null) {
            mList.clear();
        } else {
            mList = new ArrayList<>();
        }
    }

    private void setupFavoriteViewModel() {
        FavoriteMovieViewModel favoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        favoriteMovieViewModel.getMovies().observe(this, favoriteML -> {
            if(favoriteML.size()>0) {
                favoriteMoviesList.clear();
                favoriteMoviesList = favoriteML;
            }
            loadFavMovies();
        });
    }
}
