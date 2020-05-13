package com.example.popularmovies.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 13-05-2020 02:02 PM
 */
@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)

public abstract class FavoriteMovieDatabase extends RoomDatabase {
    private static final String TAG = "FavoriteMovieDatabase";
    private static final Object LOCKObj = new Object();
    private static final String DATABASE_NAME = "FavoriteMoviesDataBase";
    private static FavoriteMovieDatabase sInstance;

    public static FavoriteMovieDatabase getInstance(Context context) {
        Log.d(TAG, "getInstance: FavoriteMovieDatabase");
        if (sInstance == null) {
            synchronized (LOCKObj) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, FavoriteMovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteMovieDAO movieDao();
}
