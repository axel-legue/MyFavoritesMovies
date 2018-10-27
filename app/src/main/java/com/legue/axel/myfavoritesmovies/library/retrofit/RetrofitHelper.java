package com.legue.axel.myfavoritesmovies.library.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.MyFavoritesMoviesApplication;
import com.legue.axel.myfavoritesmovies.library.response.MoviesResponse;
import com.legue.axel.myfavoritesmovies.library.response.ReviewsResponse;
import com.legue.axel.myfavoritesmovies.library.response.TrailersResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RetrofitHelper {

    private static final String TAG = RetrofitHelper.class.getSimpleName();

    public static void getPopularMovies(String ApiKey,String page, String language, final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {

        application.getRetrofitManager().getPopularMovies(ApiKey,page, language)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe :" + d.toString());
                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                        if (moviesResponse != null) {
                            application.setMoviesResponse(moviesResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            Log.i(TAG, "Server respond with code : " + code);
                            Log.i(TAG, "Response : " + httpException.getMessage());
                        } else {
                            Log.i(TAG, e.getMessage() == null ? "unknown error" : e.getMessage());
                            e.printStackTrace();
                        }
                        // Send message for send image
                        Message msg = new Message();
                        msg.what = Constants.ACTION_ERROR;
                        msg.obj = Constants.ERROR;
                        handlerMessage.sendMessage(msg);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                        Message message = new Message();
                        message.what = action;
                        handlerMessage.sendMessage(message);
                    }
                });
    }

    public static void getTopRatedMovies(String ApiKey,String page, String language, final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {

        application.getRetrofitManager().getTopRatedMovies(ApiKey,page, language)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe :" + d.toString());
                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                        if (moviesResponse != null) {
                            application.setMoviesResponse(moviesResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            Log.i(TAG, "Server respond with code : " + code);
                            Log.i(TAG, "Response : " + httpException.getMessage());
                        } else {
                            Log.i(TAG, e.getMessage() == null ? "unknown error" : e.getMessage());
                            e.printStackTrace();
                        }
                        // Send message for send image
                        Message msg = new Message();
                        msg.what = Constants.ACTION_ERROR;
                        msg.obj = Constants.ERROR;
                        handlerMessage.sendMessage(msg);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                        Message message = new Message();
                        message.what = action;
                        handlerMessage.sendMessage(message);
                    }
                });
    }

    public static void getTrailersMovie(String ApiKey,int movieId, String language, final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {
        application.getRetrofitManager().getTrailersMovie(ApiKey,movieId, language)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrailersResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe :" + d.toString());
                    }

                    @Override
                    public void onNext(TrailersResponse trailersResponse) {
                        if (trailersResponse != null) {
                            application.setTrailersResponse(trailersResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            Log.i(TAG, "Server respond with code : " + code);
                            Log.i(TAG, "Response : " + httpException.getMessage());
                        } else {
                            Log.i(TAG, e.getMessage() == null ? "unknown error" : e.getMessage());
                            e.printStackTrace();
                        }
                        // Send message for send image
                        Message msg = new Message();
                        msg.what = Constants.ACTION_ERROR;
                        msg.obj = Constants.ERROR;
                        handlerMessage.sendMessage(msg);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                        Message message = new Message();
                        message.what = action;
                        handlerMessage.sendMessage(message);
                    }
                });
    }

    public static void getReviewsMovie(String ApiKey,int movieId, String language, String page, final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {
        application.getRetrofitManager().getReviewsMovie(ApiKey, movieId, page, language)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe :" + d.toString());
                    }

                    @Override
                    public void onNext(ReviewsResponse reviewsResponse) {
                        if (reviewsResponse != null) {
                            application.setReviewResponse(reviewsResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            int code = httpException.code();
                            Log.i(TAG, "Server respond with code : " + code);
                            Log.i(TAG, "Response : " + httpException.getMessage());
                        } else {
                            Log.i(TAG, e.getMessage() == null ? "unknown error" : e.getMessage());
                            e.printStackTrace();
                        }
                        // Send message for send image
                        Message msg = new Message();
                        msg.what = Constants.ACTION_ERROR;
                        msg.obj = Constants.ERROR;
                        handlerMessage.sendMessage(msg);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                        Message message = new Message();
                        message.what = action;
                        handlerMessage.sendMessage(message);
                    }
                });
    }
}
