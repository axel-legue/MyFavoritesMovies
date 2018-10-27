package com.legue.axel.myfavoritesmovies.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.legue.axel.myfavoritesmovies.database.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> loadAllFavoriteMovie();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<Movie> getMovieByid(int movieId);

    @Insert
    void inserMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);
}
