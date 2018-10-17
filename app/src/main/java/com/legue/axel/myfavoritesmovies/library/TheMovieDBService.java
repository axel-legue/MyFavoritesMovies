package com.legue.axel.myfavoritesmovies.library;

import com.legue.axel.myfavoritesmovies.model.MoviesResponse;
import com.legue.axel.myfavoritesmovies.model.ReviewsResponse;
import com.legue.axel.myfavoritesmovies.model.TrailersResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TheMovieDBService {

    @GET("/3/movie/popular")
    Observable<MoviesResponse> getPopularMovies(@QueryMap Map<String, String> mapString);

    @GET("/3/movie/top_rated")
    Observable<MoviesResponse> getTopRatedMovies(@QueryMap Map<String, String> mapString);

    @GET("/3/movie/{id}/videos")
    Observable<TrailersResponse> getTrailerMovie(@Path("id") int idMovie, @QueryMap Map<String, String> stringMap);

    @GET("/3/movie/{id}/reviews")
    Observable<ReviewsResponse> getReviewsMovie(@Path("id") int idMovie, @QueryMap Map<String, String> stringMap);
}
