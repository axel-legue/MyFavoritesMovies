package com.legue.axel.myfavoritesmovies.library.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.legue.axel.myfavoritesmovies.Constants;
import com.legue.axel.myfavoritesmovies.MyFavoritesMoviesApplication;
import com.legue.axel.myfavoritesmovies.model.MoviesResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RetrofitHelper {

    private static final String TAG = RetrofitHelper.class.getSimpleName();

    public static void getPopularMovies(String page, String language,final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {

        application.getRetrofitManager().getPopularMovies(page, language)
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

    public static void getTopRatedMovies(String page, String language,final int action, final Handler handlerMessage, final MyFavoritesMoviesApplication application) {

        application.getRetrofitManager().getTopRatedMovies(page, language)
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
}
