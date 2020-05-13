package com.example.popularmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 13-05-2020 02:00 PM
 */
@Dao
public interface FavoriteMovieDAO {
    @Query("SELECT * FROM FavoriteMovies ORDER BY id")
    LiveData<List<FavoriteMovie>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteMovie favMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovie favMovie);

    @Delete
    void deleteMovie(FavoriteMovie favMovie);

    @Query("SELECT * FROM FavoriteMovies WHERE id = :id")
    FavoriteMovie loadMovieById(int id);
}
