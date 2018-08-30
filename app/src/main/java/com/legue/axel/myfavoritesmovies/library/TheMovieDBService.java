package com.legue.axel.myfavoritesmovies.library;

import com.legue.axel.myfavoritesmovies.model.MoviesResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TheMovieDBService {

    @GET("/3/movie/popular")
    Observable<MoviesResponse> getPopularMovies(@QueryMap Map<String,String> mapString);

    @GET("/3/movie/top_rated")
    Observable<MoviesResponse> getTopRatedMovies(@QueryMap Map<String,String> mapString);
}
