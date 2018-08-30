package com.legue.axel.myfavoritesmovies.library;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TheMovieDBService {

    @GET("/3/movie/popular")
    Call<ResponseBody> getPopularMovies(@QueryMap Map<String,String> mapString);

    @GET("/3/movie/top_rated")
    Call<ResponseBody> getTopRatedMovies(@QueryMap Map<String,String> mapString);
}
