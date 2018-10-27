package com.legue.axel.myfavoritesmovies.library.retrofit;

import android.util.Log;

import com.legue.axel.myfavoritesmovies.library.TheMovieDBService;
import com.legue.axel.myfavoritesmovies.library.response.MoviesResponse;
import com.legue.axel.myfavoritesmovies.library.response.ReviewsResponse;
import com.legue.axel.myfavoritesmovies.library.response.TrailersResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String TAG = RetrofitManager.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String PAGE_KEY = "page";
    private static final String LANGUAGE_KEY = "language";
    private static final String API_KEY = "api_key";
    private final TheMovieDBService movieDBService;
    private final Retrofit retrofit;

    public RetrofitManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        movieDBService = retrofit.create(TheMovieDBService.class);
    }


    public Observable<MoviesResponse> getPopularMovies(String apiKey,String page, String language) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY, apiKey);
        queryParams.put(LANGUAGE_KEY, language);
        queryParams.put(PAGE_KEY, page);
        Log.i(TAG, "getPopularMovies: " + page);
        Log.i(TAG, "getPopularMovies: " + language);
        Log.i(TAG, "getPopularMovies: " + apiKey);

        return movieDBService.getPopularMovies(queryParams);
    }

    public Observable<MoviesResponse> getTopRatedMovies(String apiKey,String page, String language) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY, apiKey);
        queryParams.put(LANGUAGE_KEY, language);
        queryParams.put(PAGE_KEY, page);

        return movieDBService.getTopRatedMovies(queryParams);
    }

    public Observable<TrailersResponse> getTrailersMovie(String apiKey,int movieId, String language) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY, apiKey);
        queryParams.put(LANGUAGE_KEY, language);

        return movieDBService.getTrailerMovie(movieId, queryParams);
    }

    public Observable<ReviewsResponse> getReviewsMovie(String apiKey, int movieId, String page, String language) {
        Log.i(TAG, "getReviewsMovie: ");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(API_KEY, apiKey);
        queryParams.put(LANGUAGE_KEY, language);
        queryParams.put(PAGE_KEY, page);

        return movieDBService.getReviewsMovie(movieId, queryParams);
    }


    public Retrofit getRetrofit() {
        return retrofit;
    }
}
