package com.legue.axel.myfavoritesmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.legue.axel.myfavoritesmovies.adapter.MovieAdapter;
import com.legue.axel.myfavoritesmovies.database.MyFavoritesMoviesDatabase;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitHelper;
import com.legue.axel.myfavoritesmovies.database.model.Movie;
import com.legue.axel.myfavoritesmovies.library.response.MoviesResponse;

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
    private MyFavoritesMoviesDatabase mDatabase;
    private int mCurrentPage = 1;
    private boolean topRatedSelected = false;
    private boolean popularSelected = false;
    private boolean favoriteSelected = false;
    private boolean isScrolling = false;

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.pb_load_movies)
    ProgressBar mLoadingProgressBar;

    private MovieAdapter.MovieListener mMovieListener = new MovieAdapter.MovieListener() {
        @Override
        public void movieSelected(int position, Movie movie, View sharedImageView) {
            if (movie != null) {
                // Convert the Movie selected to Json String format
                Gson gson = new Gson();
                String dataMovieJson = gson.toJson(movie);

                //Start Activity that will show movie details
                Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);

                // Get the transition name from the string
                String transitionName = MainActivity.this.getString(R.string.poster_transition);

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MainActivity.this, sharedImageView, transitionName);
                intent.putExtra(Constants.EXTRA_MOVIE, dataMovieJson);

                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        }

        @Override
        public void startScroll(int page) {
            isScrolling = true;

            if (topRatedSelected) {
                loadTopRatedMovies(page);
            } else if (popularSelected) {
                loadPopularMovies(page);
            } else {
                // TODO : Add query 20 by 20
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initClickListener();
        initData();

        // TODO : Save the current filter selected and restore it on rotation
        // TODO : Adjust Design ( Favorite Star / Remove Separator line on last element  / Set of color / Police Size )
        // TODO : Add Infinite Scroll
        // TODO : Check internet connexion and display a warning if no connexion
        // TODO : Load Favorite Movies if no connexion
        // TODO : Display Message if list are empty
        // TODO : Change Background of Main activity to black
        // TODO : Add Transition onScroll down/up on  DetailMovieActivity
        // TODO : Find a better way to display Trailer ?

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
                isScrolling = false;
                mMovieAdapter.setPage(1);
                loadTopRatedMovies(mCurrentPage);
                break;
            case R.id.menu_popular_movies:
                isScrolling = false;
                mMovieAdapter.setPage(1);
                loadPopularMovies(mCurrentPage);
                break;

            case R.id.menu_favorites_movies:
                isScrolling = false;
                loadFavoritesMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initClickListener() {
    }

    @Override
    public void initData() {

        mDatabase = MyFavoritesMoviesDatabase.getsInstance(getApplicationContext());

        application = (MyFavoritesMoviesApplication) getApplication();

        if (movieList == null) {
            movieList = new ArrayList<>();
        }

        mMovieAdapter = new MovieAdapter(this, movieList, mMovieListener);
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        mMoviesRecyclerView.setHasFixedSize(true);

        loadPopularMovies(mCurrentPage);
    }

    private void loadPopularMovies(Integer page) {
        popularSelected = true;
        topRatedSelected = false;
        favoriteSelected = false;

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getPopularMovies(
                page.toString(),
                Constants.LANGUAGE_US,
                Constants.ACTION_COMPLETE,
                moviesHandler,
                application);
    }

    private void loadTopRatedMovies(Integer page) {
        popularSelected = false;
        topRatedSelected = true;
        favoriteSelected = false;

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getTopRatedMovies(
                page.toString(),
                Constants.LANGUAGE_US,
                Constants.ACTION_COMPLETE,
                moviesHandler,
                application);
    }

    private void loadFavoritesMovies() {
        popularSelected = false;
        topRatedSelected = false;
        favoriteSelected = true;

        LiveData<List<Movie>> favoritesMovies = mDatabase.movieDao().loadAllFavoriteMovie();
        favoritesMovies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    movieList.clear();
                    movieList.addAll(movies);
                    mMovieAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private Handler moviesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.ACTION_COMPLETE:
                    mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    if (application.getMoviesResponse() != null) {
                        moviesResponse = application.getMoviesResponse();
                        if (!isScrolling) {
                            movieList.clear();
                        }
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
