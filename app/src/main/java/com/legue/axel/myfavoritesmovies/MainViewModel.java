package com.legue.axel.myfavoritesmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.legue.axel.myfavoritesmovies.database.MyFavoritesMoviesDatabase;
import com.legue.axel.myfavoritesmovies.database.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favoritesMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MyFavoritesMoviesDatabase myFavoritesMoviesDatabase = MyFavoritesMoviesDatabase.getsInstance(this.getApplication());
        favoritesMovies = myFavoritesMoviesDatabase.movieDao().loadAllFavoriteMovie();
    }

    public LiveData<List<Movie>> getFavoritesMovies() {
        return favoritesMovies;
    }
}
