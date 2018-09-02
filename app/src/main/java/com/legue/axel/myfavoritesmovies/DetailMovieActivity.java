package com.legue.axel.myfavoritesmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity implements ActivityInterface {
    private static final String TAG = DetailMovieActivity.class.getSimpleName();
    private Movie mMovieSelected;

    @BindView(R.id.imageView)
    private ImageView imageView;
    @BindView(R.id.tv_title)
    private TextView mTitleTextView;
    @BindView(R.id.tv_release_date)
    private TextView mReleaseDateTextView;
    @BindView(R.id.tv_original_title)
    private TextView mOriginalTitleTextView;
    @BindView(R.id.tv_vote)
    private TextView mVoteTextView;
    @BindView(R.id.tv_synopsis)
    private TextView mSynopsisTextView;


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

        Intent intent = getIntent();

        if (intent.hasExtra(Constants.EXTRA_MOVIE)) {

            Gson gson = new Gson();
            mMovieSelected = gson.fromJson(intent.getStringExtra(Constants.EXTRA_MOVIE), Movie.class);
            populateUI(mMovieSelected);

            String imageUrl = Constants.BASE_IMAGE_UL + Constants.IMAGE_QUALITY_W500 + mMovieSelected.getBackdropPath();

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
        }


    }

    private void populateUI(Movie mMovieSelected) {
        mTitleTextView.setText(mMovieSelected.getTitle());
        mOriginalTitleTextView.setText(mMovieSelected.getOriginalTitle());
        mReleaseDateTextView.setText(mMovieSelected.getReleaseDate());
        mVoteTextView.setText(String.valueOf(mMovieSelected.getVoteAverage()));
        mSynopsisTextView.setText(mMovieSelected.getOverview());
    }
}
