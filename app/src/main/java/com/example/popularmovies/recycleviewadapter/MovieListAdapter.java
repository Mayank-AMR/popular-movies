package com.example.popularmovies.recycleviewadapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.network.ApiHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private static final String TAG = "MovieListAdapter";
    private List<Movie> movieList;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup;
    private MovieItemClickListener movieItemClickListener;

    public MovieListAdapter(List<Movie> movieList, MovieItemClickListener movieItemClickListener) {
        Log.d(TAG, "MovieListAdapter: start");
        this.movieList = movieList;
        this.movieItemClickListener = movieItemClickListener;
        spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d(TAG, "getSpanSize: end");
                return (position % 8 < 2 ? 3 : 2);
            }
        };
    }

    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: start");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_movie, parent, false);
        Log.d(TAG, "onCreateViewHolder: end");
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListAdapter.MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: start");
        final Movie movie = movieList.get(position);
        String posterUrl = ApiHandler.posterUri(movie.getPosterPath());

        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.ic_image_holder)
                .error(R.drawable.ic_error)
                .into(holder.posterIV);

        ViewCompat.setTransitionName(holder.posterIV, movie.getTitle());

        holder.view.setOnClickListener(v -> movieItemClickListener.onMovieClicked(holder.getAdapterPosition(), movie, holder.posterIV));
        Log.d(TAG, "onBindViewHolder: end");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + (movieList == null ? 0 : movieList.size()));
        return (movieList == null ? 0 : movieList.size());
    }
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void getSpanSizeLookup(GridLayoutManager layoutManager) {
        Log.d(TAG, "getSpanSizeLookup: ");
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 5) {
                    case 0:
                    case 1:
                        return 3;
                    case 2:
                    case 3:
                    case 4:
                        return 2;
                }
                throw new IllegalStateException("internal error");
            }
        });
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieViewHolder";
        View view;
        ImageView posterIV;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "MovieViewHolder: start");
            view = itemView;
            posterIV = view.findViewById(R.id.poster_iv);
            Log.d(TAG, "MovieViewHolder: end");
        }
    }
}
