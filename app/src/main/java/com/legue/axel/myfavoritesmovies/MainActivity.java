package com.legue.axel.myfavoritesmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.adapter.MovieAdapter;
import com.legue.axel.myfavoritesmovies.database.MyFavoritesMoviesDatabase;
import com.legue.axel.myfavoritesmovies.database.model.Movie;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.library.response.MoviesResponse;
import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    /**
     * ===========================================================================================
     * GENERAL UPDATE TO MAKE FOR A BETTER APPLICATION
     * =============================================================================================
     */
    // TODO : Adjust Design ( Remove Separator line on last element  / Set of color / Police Size )
    // TODO : Display Message if list are empty / error
    // TODO : Add Transition onScroll down/up on  DetailMovieActivity
    // TODO : Find a better way to display Trailer ?
    // TODO : fine tune the workflow around connectivity

    private final static String TAG = MainActivity.class.getSimpleName();

    private MyFavoritesMoviesApplication application;
    private MoviesResponse moviesResponse;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieList;
    LiveData<List<Movie>> favoritesMovies;
    private MyFavoritesMoviesDatabase mDatabase;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private MenuItem mMenuItem;
    private int menuIdSelected = -1;
    private static final String KEY_MENU_SELECTED = "key_menu";

    private int mCurrentPage = 1;
    private boolean topRatedSelected = false;
    private boolean popularSelected = false;
    private boolean favoriteSelected = false;
    private boolean isScrolling = false;
    private boolean isNetworkAvailable = false;
    String API_KEY_VALUE;

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
                // TODO : Add query 20 by 20 ?
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        API_KEY_VALUE = getString(R.string.API_KEY);

        ButterKnife.bind(this);

        initClickListener();

        if (savedInstanceState != null) {
            menuIdSelected = savedInstanceState.getInt(KEY_MENU_SELECTED);
        }
        mNetworkChangeReceiver = new NetworkChangeReceiver();

        initData();
    }

    private void registerNetworkChangeReceiver() {
        Log.i(TAG, "registerNetworkChangeReceiver: ");
        if (mNetworkChangeReceiver != null) {
            registerReceiver(mNetworkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterNetWorkChangeReceiver() {
        Log.i(TAG, "unregisterNetWorkChangeReceiver: ");
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        registerNetworkChangeReceiver();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        unregisterNetWorkChangeReceiver();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putInt(KEY_MENU_SELECTED, menuIdSelected);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");
        if (savedInstanceState.containsKey(KEY_MENU_SELECTED)) {
            menuIdSelected = savedInstanceState.getInt(KEY_MENU_SELECTED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.menu, menu);
        if (menuIdSelected == -1) {
            return true;
        }
        switch (menuIdSelected) {
            case R.id.menu_popular_movies:
                mMenuItem = menu.findItem(R.id.menu_popular_movies);
                mMenuItem.setChecked(true);
                loadPopularMovies(mCurrentPage);
                break;
            case R.id.menu_top_rated_movies:
                mMenuItem = menu.findItem(R.id.menu_top_rated_movies);
                mMenuItem.setChecked(true);
                loadTopRatedMovies(mCurrentPage);
                break;
            case R.id.menu_favorites_movies:
                mMenuItem = menu.findItem(R.id.menu_favorites_movies);
                mMenuItem.setChecked(true);
                loadFavoritesMovies();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_top_rated_movies:
                menuIdSelected = id;
                item.setChecked(true);
                isScrolling = false;
                mMovieAdapter.setPage(1);
                if (isNetworkAvailable) {
                    loadTopRatedMovies(mCurrentPage);
                } else {
                    Toast.makeText(application, "You need internet access for this", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_popular_movies:
                isScrolling = false;
                menuIdSelected = id;
                item.setChecked(true);
                mMovieAdapter.setPage(1);
                if (isNetworkAvailable) {
                    loadPopularMovies(mCurrentPage);
                } else {
                    Toast.makeText(application, "You need internet access for this", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_favorites_movies:
                isScrolling = false;
                menuIdSelected = id;
                item.setChecked(true);
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
        Log.i(TAG, "initData: ");
        mDatabase = MyFavoritesMoviesDatabase.getsInstance(getApplicationContext());

        application = (MyFavoritesMoviesApplication) getApplication();

        if (movieList == null) {
            movieList = new ArrayList<>();
        }

        mMovieAdapter = new MovieAdapter(this, movieList, mMovieListener);
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        mMoviesRecyclerView.setHasFixedSize(true);

    }

    private void displayNoInternetDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("No network detected")
                .setMessage("For optimal use of the application, connect to the internet")
                .setCancelable(false)
                .setNeutralButton("Ok", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void loadPopularMovies(Integer page) {
        Log.i(TAG, "loadPopularMovies: ");
        popularSelected = true;
        topRatedSelected = false;
        favoriteSelected = false;

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getPopularMovies(
                API_KEY_VALUE,
                page.toString(),
                Constants.LANGUAGE_US,
                Constants.ACTION_COMPLETE,
                moviesHandler,
                application);
    }

    private void loadTopRatedMovies(Integer page) {
        Log.i(TAG, "loadTopRatedMovies: ");
        popularSelected = false;
        topRatedSelected = true;
        favoriteSelected = false;

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        RetrofitHelper.getTopRatedMovies(
                API_KEY_VALUE,
                page.toString(),
                Constants.LANGUAGE_US,
                Constants.ACTION_COMPLETE,
                moviesHandler,
                application);
    }

    private void loadFavoritesMovies() {
        Log.i(TAG, "loadFavoritesMovies: ");
        popularSelected = false;
        topRatedSelected = false;
        favoriteSelected = true;
        mLoadingProgressBar.setVisibility(View.VISIBLE);

        // Pour récupèrer le ViewModel on doit appeler le fournisseur des ViewModel pour cette activité et passer
        // la classe ViewModel en paramètre.
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        /** REPLACED BY VIEW MODEL :
        favoritesMovies = mDatabase.movieDao().loadAllFavoriteMovie();
        favoritesMovies.observe(this, movies -> {
            if (movies != null && movies.size() > 0) {
                mLoadingProgressBar.setVisibility(View.INVISIBLE);
                movieList.clear();
                movieList.addAll(movies);
                mMovieAdapter.notifyDataSetChanged();
            }
        });
         **/
        // Maintenant on peut récupèrer notre LiveData object en utilisant la méthode  "getFavoritesMovies()"
        // de notre ViewModel
        mainViewModel.getFavoritesMovies().observe(this, movies -> {
            if (movies != null && movies.size() > 0) {
                Log.i(TAG, "loadFavoritesMovies from ViewModel: ");
                mLoadingProgressBar.setVisibility(View.INVISIBLE);
                movieList.clear();
                movieList.addAll(movies);
                mMovieAdapter.notifyDataSetChanged();
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

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive ");
            try {
                if (isOnline(context)) {
                    if (movieList == null || movieList.size() == 0) {
                        loadPopularMovies(mCurrentPage);
                    }
                    isNetworkAvailable = true;
                    //     Toast.makeText(context, "Network ON", Toast.LENGTH_SHORT).show();
                } else {
                    displayNoInternetDialog();
                    loadFavoritesMovies();
                    isNetworkAvailable = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "onReceive error : " + e.toString());
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = null;
                try {
                    netInfo = cm.getActiveNetworkInfo();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.d(TAG, "isOnline: ConnectivityManager.getActiveNetworkInfo() is null");
                }
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
