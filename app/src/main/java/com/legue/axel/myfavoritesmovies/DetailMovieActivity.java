package com.legue.axel.myfavoritesmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.model.Movie;

public class DetailMovieActivity extends AppCompatActivity implements ActivityInterface {
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private Movie mMovieSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        initView();
        initClickListener();
        initData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initClickListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_MOVIE)) {
            Gson gson = new Gson();
            mMovieSelected = gson.fromJson(intent.getStringExtra(Constants.EXTRA_MOVIE), Movie.class);
        }
    }
}
