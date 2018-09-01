package com.legue.axel.myfavoritesmovies;

import android.app.Application;

import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitManager;
import com.legue.axel.myfavoritesmovies.model.MoviesResponse;

public class MyFavoritesMoviesApplication extends Application {

    private static MyFavoritesMoviesApplication instance;
    private RetrofitManager mRetrofitManager;
    private MoviesResponse moviesResponse;

    public static MyFavoritesMoviesApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mRetrofitManager = new RetrofitManager();
    }

    public RetrofitManager getRetrofitManager() {
        return mRetrofitManager;
    }

    public void setRetrofitManager(RetrofitManager mRetrofitManager) {
        this.mRetrofitManager = mRetrofitManager;
    }

    public MoviesResponse getMoviesResponse() {
        return moviesResponse;
    }

    public void setMoviesResponse(MoviesResponse moviesResponse) {
        this.moviesResponse = moviesResponse;
    }
}
