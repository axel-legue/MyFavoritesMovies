package com.legue.axel.myfavoritesmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.adapter.TrailerAdapter;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.library.retrofit.RetrofitHelper;
import com.legue.axel.myfavoritesmovies.model.Movie;
import com.legue.axel.myfavoritesmovies.model.Trailer;
import com.legue.axel.myfavoritesmovies.model.TrailersResponse;
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


    private Movie mMovieSelected;
    private MyFavoritesMoviesApplication application;
    private TrailerAdapter mTrailerAdapter;
    private TrailersResponse mTrailerResponse;
    private List<Trailer> trailerList;


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
        application = (MyFavoritesMoviesApplication) getApplication();

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

            if (trailerList == null) {
                trailerList = new ArrayList<>();
            }

            mTrailerAdapter = new TrailerAdapter(this, trailerList, mTrailerListener);
            mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            mTrailerRecyclerView.setHasFixedSize(true);

            getTrailerMovie();
        }


    }

    private void getTrailerMovie() {
        RetrofitHelper.getTrailersMovie(
                mMovieSelected.getId(),
                Constants.LANGUAGE_US,
                Constants.ACTION_COMPLETE,
                moviesHandler,
                application);
    }

    private void populateUI(Movie mMovieSelected) {
        mTitleTextView.setText(mMovieSelected.getTitle());
        mOriginalTitleTextView.setText(mMovieSelected.getOriginalTitle());
        mReleaseDateTextView.setText(mMovieSelected.getReleaseDate());
        mVoteTextView.setText(String.valueOf(mMovieSelected.getVoteAverage()));
        mSynopsisTextView.setText(mMovieSelected.getOverview());
    }

    private Handler moviesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.ACTION_COMPLETE:
                    //  mLoadingProgressBar.setVisibility(View.INVISIBLE);
                    if (application.getTrailersResponse() != null) {
                        mTrailerResponse = application.getTrailersResponse();

                        trailerList.clear();
                        trailerList.addAll(mTrailerResponse.getVideoList());
                        mTrailerAdapter.notifyDataSetChanged();

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
