package com.legue.axel.myfavoritesmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitHelper;
import com.legue.axel.myfavoritesmovies.model.Movie;
import com.legue.axel.myfavoritesmovies.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private final static String TAG = MainActivity.class.getSimpleName();

    private MyFavoritesMoviesApplication application;
    private MoviesResponse moviesResponse;
    private MovieAdapter mMovieAdapter;
    private List<Movie> movieList;

    @BindView(R.id.rv_movies)
    private RecyclerView mMoviesRecyclerView;
    @BindView(R.id.pb_load_movies)
    private ProgressBar mLoadingProgressBar;

    private MovieAdapter.MovieListener mMovieListener = (position, movie, sharedImageView) -> {
        if (movie != null) {
            // Convert the Movie selected to Json String format
            Gson gson = new Gson();
            String dataMovieJson = gson.toJson(movie);

            //Start Activity that will show movie details
            Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);

            // Get the transition name from the string
            String transitionName = getString(R.string.poster_transition);

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, sharedImageView, transitionName);
            intent.putExtra(Constants.EXTRA_MOVIE, dataMovieJson);

            ActivityCompat.startActivity(this, intent, options.toBundle());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initClickListener();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_top_rated_movies:
                loadTopRatedMovies(1);
                break;
            case R.id.menu_popular_movies:
                loadPopularMovies(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initClickListener() {
    }

    @Override
    public void initData() {
        application = (MyFavoritesMoviesApplication) getApplication();

        if (movieList == null) {
            movieList = new ArrayList<>();
        }

        mMovieAdapter = new MovieAdapter(this, movieList, mMovieListener);
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        mMoviesRecyclerView.setHasFixedSize(true);

        loadPopularMovies(1);
    }

    private void loadPopularMovies(Integer page) {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getPopularMovies(page.toString(), Constants.LANGUAGE_US, Constants.ACTION_COMPLETE, moviesHandler, application);
    }

    private void loadTopRatedMovies(Integer page) {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getTopRatedMovies(page.toString(), Constants.LANGUAGE_US, Constants.ACTION_COMPLETE, moviesHandler, application);
    }

    private Handler moviesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.ACTION_COMPLETE:
                    mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    if (application.getMoviesResponse() != null) {
                        moviesResponse = application.getMoviesResponse();
                        movieList.clear();
                        movieList.addAll(moviesResponse.getMovieList());
                        mMovieAdapter.notifyDataSetChanged();

                    }
                    break;

                case Constants.ACTION_ERROR:
                    mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
