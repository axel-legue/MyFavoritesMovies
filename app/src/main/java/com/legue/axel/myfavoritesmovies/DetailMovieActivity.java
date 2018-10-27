package com.legue.axel.myfavoritesmovies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.adapter.ReviewAdapter;
import com.legue.axel.myfavoritesmovies.adapter.TrailerAdapter;
import com.legue.axel.myfavoritesmovies.database.MyFavoritesMoviesDatabase;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitHelper;
import com.legue.axel.myfavoritesmovies.database.model.Movie;
import com.legue.axel.myfavoritesmovies.database.model.Review;
import com.legue.axel.myfavoritesmovies.library.response.ReviewsResponse;
import com.legue.axel.myfavoritesmovies.database.model.Trailer;
import com.legue.axel.myfavoritesmovies.library.response.TrailersResponse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity implements ActivityInterface {
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_original_title)
    TextView mOriginalTitleTextView;
    @BindView(R.id.tv_vote)
    TextView mVoteTextView;
    @BindView(R.id.tv_synopsis)
    TextView mSynopsisTextView;
    @BindView(R.id.rv_trailers)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewRecyclerView;
    @BindView(R.id.iv_add_favorite)
    ImageView mAddToFavorite;

    private Movie mMovieSelected;
    private MyFavoritesMoviesApplication mApplication;

    private TrailerAdapter mTrailerAdapter;
    private TrailersResponse mTrailerResponse;
    private List<Trailer> mTrailerList;

    private ReviewAdapter mReviewAdapter;
    private ReviewsResponse mReviewResponse;
    private List<Review> mReviewList;

    private MyFavoritesMoviesDatabase mDatabase;

    private LiveData<Movie> dbMovie;
    private boolean isFavorite;

    TrailerAdapter.TrailerListener mTrailerListener = intent -> startActivity(intent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        supportPostponeEnterTransition();

        ButterKnife.bind(this);
        initData();
        initClickListener();
    }

    @Override
    public void initClickListener() {

        mAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    mAddToFavorite.setColorFilter(R.color.myFavoriteColorPrimary);
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Delete movie in database");
                            mDatabase.movieDao().deleteMovie(mMovieSelected);
                        }
                    });
                } else {
                    mAddToFavorite.setColorFilter(R.color.cardview_dark_background);

                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Insert movie in database");
                            mDatabase.movieDao().inserMovie(mMovieSelected);
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        mDatabase = MyFavoritesMoviesDatabase.getsInstance(getApplicationContext());
        mApplication = (MyFavoritesMoviesApplication) getApplication();

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_MOVIE)) {
            Gson gson = new Gson();
            mMovieSelected = gson.fromJson(intent.getStringExtra(Constants.EXTRA_MOVIE), Movie.class);
            populateUI(mMovieSelected);

            String imageUrl = Constants.BASE_IMAGE_URL + Constants.IMAGE_QUALITY_W500 + mMovieSelected.getBackdropPath();
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });

            setTrailerAdapter();
            getTrailerMovie();
            setReviewAdapter();
            getReviewsMovie();
            isFavorite();

        }


    }

    private void isFavorite() {
        dbMovie = mDatabase.movieDao().getMovieByid(mMovieSelected.getId());
        dbMovie.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie == null) {
                    isFavorite = false;
                    mAddToFavorite.setImageDrawable(ContextCompat.getDrawable(mApplication, R.drawable.ic_favorite_outline_border_24px));
                } else {
                    isFavorite = true;
                    mAddToFavorite.setImageDrawable(ContextCompat.getDrawable(mApplication, R.drawable.ic_favorite_outline_24px));
                }
            }
        });
    }


    private void setTrailerAdapter() {
        if (mTrailerList == null) {
            mTrailerList = new ArrayList<>();
        }
        mTrailerAdapter = new TrailerAdapter(this, mTrailerList, mTrailerListener);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.setHasFixedSize(true);
    }

    private void getTrailerMovie() {
        RetrofitHelper.getTrailersMovie(
                mMovieSelected.getId(),
                Constants.LANGUAGE_US,
                Constants.ACTION_GET_TRAILER,
                moviesHandler,
                mApplication);
    }

    private void setReviewAdapter() {
        if (mReviewList == null) {
            mReviewList = new ArrayList<>();
        }
        DividerItemDecoration mItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        try {
            mItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.review_divider));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to get drawable divider");
        }

        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.addItemDecoration(mItemDecoration);
        mReviewRecyclerView.setHasFixedSize(true);
    }


    private void getReviewsMovie() {
        RetrofitHelper.getReviewsMovie(
                mMovieSelected.getId(),
                Constants.LANGUAGE_US,
                "1",
                Constants.ACTION_GET_REVIEWS,
                moviesHandler,
                mApplication);
    }

    private void populateUI(Movie mMovieSelected) {
        mTitleTextView.setText(mMovieSelected.getTitle());
        mOriginalTitleTextView.setText(mMovieSelected.getOriginalTitle());
        mReleaseDateTextView.setText(mMovieSelected.getReleaseDate());
        mVoteTextView.setText(String.valueOf(mMovieSelected.getVoteAverage()));
        mSynopsisTextView.setText(mMovieSelected.getOverview());
    }

    @SuppressLint("HandlerLeak")
    private Handler moviesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.ACTION_GET_TRAILER:
                    Log.i(TAG, "handleMessage: ACTION_GET_TRAILER");
                    //  mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    if (mApplication.getTrailersResponse() != null) {
                        mTrailerResponse = mApplication.getTrailersResponse();

                        mTrailerList.clear();
                        mTrailerList.addAll(mTrailerResponse.getVideoList());
                        mTrailerAdapter.notifyDataSetChanged();
                    }
                    break;

                case Constants.ACTION_GET_REVIEWS:
                    Log.i(TAG, "handleMessage: ACTION_GET_REVIEWS");
                    //  mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    if (mApplication.getReviewResponse() != null) {
                        mReviewResponse = mApplication.getReviewResponse();

                        mReviewList.clear();
                        mReviewList.addAll(mReviewResponse.getReviewList());
                        mReviewAdapter.notifyDataSetChanged();
                    }
                    break;

                case Constants.ACTION_ERROR:
                    //   mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
