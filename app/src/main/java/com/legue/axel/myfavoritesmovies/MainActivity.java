package com.legue.axel.myfavoritesmovies;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.legue.axel.myfavoritesmovies.library.RetrofitHelper;
import com.legue.axel.myfavoritesmovies.model.Movie;
import com.legue.axel.myfavoritesmovies.model.MovieAdapter;
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
    RecyclerView mMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
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
            case R.id.menu_order:
                Toast.makeText(this, "order selected", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
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

        loadPopularMovies();

        // Mock Data for Adapter
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            movie.setTitle("Movie " + i);
            movieList.add(movie);
        }


        mMovieAdapter = new MovieAdapter(this, movieList);
        //TODO : replace number of column with constant.
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        mMoviesRecyclerView.setHasFixedSize(true);


    }

    private void loadPopularMovies() {
        RetrofitHelper.getPopularMovies("1", "en_US", Constants.ACTION_COMPLETE, moviesHandler, application);
    }

    private Handler moviesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.ACTION_COMPLETE:
                    if (application.getMoviesResponse() != null) {
                        //TODO fill the list of movies
                        Log.i(TAG, "handleMessage: ");
                        moviesResponse = application.getMoviesResponse();
                        //TODO : Convert Movie Response to Movies
                    }
                    break;

                case Constants.ACTION_ERROR:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
