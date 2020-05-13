package com.example.popularmovies;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.popularmovies.db.FavoriteMovie;
import com.example.popularmovies.db.FavoriteMovieDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Videos;
import com.example.popularmovies.network.ApiHandler;
import com.example.popularmovies.recycleviewadapter.ReviewListAdapter;
import com.example.popularmovies.recycleviewadapter.VideoListAdapter;
import com.example.popularmovies.utils.DBExecutor;
import com.example.popularmovies.utils.ReviewJSONUtil;
import com.example.popularmovies.utils.VideosJSONUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    int id;
    String title;
    String overview;
    String poster_path;
    String backdrop_path;
    double popularity;
    double vote_average;
    int vote_count;
    String release_date;
    String trName;

    TextView overviewTV;
    RatingBar ratingBar;
    TextView averageVoteTV;
    TextView voteCountTV;
    TextView releasedDateTV;
    ImageView posterIV;
    ImageView backdropIV;

    public static final String EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME";
    public static final String EXTRA_PARSABLE_MOVIE = "EXTRA_PARSABLE_MOVIE";
    public static final String EXTRA_AOC = "EXTRA_AOC";
    //----------------------------------------------------------------------------------------------
    //Stage 2nd

    private RequestQueue videoRequestQueue;
    List<Videos> videosList;
    List<Review> reviewList;

    /*@BindView(R.id.videos_rv) */ RecyclerView videoRecyclerView;
    RecyclerView reviewRecyclerView;
    private SQLiteDatabase mFavoriteMovieDB;
    private Movie mMovie;
    public FloatingActionButton fab;

    //Favorite movies
    private FavoriteMovieDatabase mFavoriteMovieDatabase;
    private ImageView mFavButton;
    private Boolean isFavorite = false;


    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting extras from Intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            getExtraFromBundle(extras);

            // Request videos API.
            videosList = new ArrayList<>();
            videoRequestQueue = Volley.newRequestQueue(this);
            requestVideo(ApiHandler.videoUri(id));

            // Requesting Review API
            reviewList = new ArrayList<>();
            requestReview(ApiHandler.reviewUri(id));

            // Adding features to FAB.
            floatingActionFavoriteButton();
        } else {
            // Bundle is null
            Log.d(TAG, "onCreate: bundle null");
        }
        // Initialising views
        overviewTV = findViewById(R.id.over_view_tv);
        ratingBar = findViewById(R.id.rating_rb);
        averageVoteTV = findViewById(R.id.average_vote_tv);
        voteCountTV = findViewById(R.id.vote_count_tv);
        releasedDateTV = findViewById(R.id.release_date_tv);
        posterIV = findViewById(R.id.poster_iv);
        backdropIV = findViewById(R.id.backdrop_iv);


        overviewTV.setText(overview);

        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.1f);
        float averageVote = (float) vote_average / 2;
        ratingBar.setRating(averageVote);

        averageVoteTV.setText(getString(R.string.average_vote, averageVote));

        String voteCnt = getResources().getQuantityString(R.plurals.vote_count, vote_count, vote_count);
        voteCountTV.setText(voteCnt);

        releasedDateTV.setText(getString(R.string.release_date, release_date));

        String posterUrl = ApiHandler.posterUri(poster_path);
        loadPoster(posterUrl);

        String backdropUrl = ApiHandler.backdropUri(backdrop_path);
        loadBackdropImage(backdropUrl);

    }

    private void loadPoster(String posterUrl) {
        // Loading movie poster image.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            posterIV.setTransitionName(trName);
        }
        Picasso.get()
                .load(posterUrl)
                .noFade()
                .into(posterIV, new Callback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onError(Exception e) {
                        startPostponedEnterTransition();
                    }
                });
    }

    private void loadBackdropImage(String backdropUrl) {
        // Loading movie Backdrop image
        Picasso.get()
                .load(backdropUrl)
                .into(backdropIV);
    }

    private void floatingActionFavoriteButton() {
        //
        fab = findViewById(R.id.fab);
        // Initialising Favorite Movie Database
        mFavoriteMovieDatabase = FavoriteMovieDatabase.getInstance(getApplicationContext());
        // Setting image resource to FAB
        fab.setImageResource(
                mMovie.isFavorite() ?
                        R.drawable.ic_favorite_black :
                        R.drawable.ic_favorite_pink);
        //
        DBExecutor.getInstance().diskIO().execute(() -> {
            final FavoriteMovie favoriteMovieItem = mFavoriteMovieDatabase.movieDao().loadMovieById(mMovie.getId());
            setFavoriteAndIcon((favoriteMovieItem != null) ? true : false);
        });

        fab.setOnClickListener(view -> {
            FavoriteMovie favoriteMovie = new FavoriteMovie(mMovie.getId(), mMovie.getTitle(),
                    mMovie.getOverview(), mMovie.getPosterPath(), mMovie.getBackdropPath(),
                    String.valueOf(mMovie.getPopularity()), String.valueOf(mMovie.getVoteAverage()), String.valueOf(mMovie.getVoteCount()),
                    mMovie.getReleaseDate());
            DBExecutor.getInstance().diskIO().execute(() -> {
                if (isFavorite) {
                    // remove item from favorite movie database
                    mFavoriteMovieDatabase.movieDao().deleteMovie(favoriteMovie);
                } else {
                    // add item in the favorite movie database.
                    mFavoriteMovieDatabase.movieDao().insertMovie(favoriteMovie);
                }
                runOnUiThread(() -> setFavoriteAndIcon(!isFavorite));
            });
        });
    }

    private void playFATIcon(FloatingActionButton fab, int fabavdres, int fallbackres) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) getResources().getDrawable(fabavdres);
            fab.setImageDrawable(avd);
            avd.start();
        } else {
            fab.setImageResource(fallbackres);
        }
    }

    private void getExtraFromBundle(Bundle bundle) {
        mMovie = bundle.getParcelable(EXTRA_PARSABLE_MOVIE);
        assert mMovie != null;
        id = mMovie.getId();
        title = mMovie.getTitle();
        overview = mMovie.getOverview();
        poster_path = mMovie.getPosterPath();
        backdrop_path = mMovie.getBackdropPath();
        popularity = mMovie.getPopularity();
        vote_average = mMovie.getVoteAverage();
        vote_count = mMovie.getVoteCount();
        release_date = mMovie.getReleaseDate();
        trName = bundle.getString(EXTRA_TRANSITION_NAME);

        setTitle(title);
    }

    //--------------------------------------------------------------------------------------------------
    private void requestVideo(String url) {
        Log.d(TAG, "requestVideo: ");
        JsonObjectRequest videoObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            Log.d(TAG, "onResponse: " + response.toString());
            videosList = VideosJSONUtil.parseVideoJson(response);

            //Calling videoRecyclerSetup() to set VideoRecyclerView with List of videos.
            videoRecyclerSetup(videosList);

        }, error -> Log.d(TAG, "onErrorResponse: " + error));

        // Adding video request from API, in queue
        videoRequestQueue.add(videoObjectRequest);
        Log.d(TAG, "requestVideo: end");
    }

    private void videoRecyclerSetup(List<Videos> videoList) {
        // Setting up VideoRecyclerView
        Log.d(TAG, "videoRecyclerSetup: start\n\n List Size --> " + videoList.size());
        VideoListAdapter videoAdapter = new VideoListAdapter(videoList);
        videoRecyclerView = findViewById(R.id.videos_rv);
        StaggeredGridLayoutManager layoutManager;
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        videoRecyclerView.setAdapter(videoAdapter);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoAdapter.notifyDataSetChanged();
        Log.d(TAG, "videoRecyclerSetup: end");
    }

    private void requestReview(String reviewUrl) {
        JsonObjectRequest reviewObjectRequest = new JsonObjectRequest(Request.Method.GET, reviewUrl, null, response -> {
            Log.d(TAG, "onResponse: " + response.toString());
            reviewList = ReviewJSONUtil.parseReviewJson(response);
            //Calling videoRecyclerSetup() to set VideoRecyclerView with List of videos.
            reviewRecyclerSetup(reviewList);
        }, error -> Log.d(TAG, "onErrorResponse: " + error));
        // Adding video request from API, in queue
        videoRequestQueue.add(reviewObjectRequest);
    }

    private void reviewRecyclerSetup(List<Review> reviewList) {
        ReviewListAdapter reviewAdapter = new ReviewListAdapter(reviewList);
        reviewRecyclerView = findViewById(R.id.review_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void setFavoriteAndIcon(Boolean favorite) {
        if (favorite) {
            isFavorite = true;
            fab.setImageResource(R.drawable.ic_favorite_pink);
        } else {
            isFavorite = false;
            fab.setImageResource(R.drawable.ic_favorite_black);
        }
    }

//--------------------------------------------------------------------------------------------------

}
