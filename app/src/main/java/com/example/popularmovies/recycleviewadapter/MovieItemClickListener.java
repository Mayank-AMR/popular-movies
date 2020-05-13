package com.example.popularmovies.recycleviewadapter;

import android.widget.ImageView;

import com.example.popularmovies.model.Movie;

public interface MovieItemClickListener {
    void onMovieClicked(int pos, Movie movie, ImageView sharedImageView);
}
