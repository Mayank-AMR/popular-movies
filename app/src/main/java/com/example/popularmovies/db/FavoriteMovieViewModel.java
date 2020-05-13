package com.example.popularmovies.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 13-05-2020 02:38 PM
 */
public class FavoriteMovieViewModel extends AndroidViewModel {
    private static final String TAG = "FavoriteMovieViewModel";

    private LiveData<List<FavoriteMovie>> movies;

    public FavoriteMovieViewModel(Application application) {
        super(application);
        FavoriteMovieDatabase database = FavoriteMovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getMovies() {
        return movies;
    }
}