package com.legue.axel.myfavoritesmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legue.axel.myfavoritesmovies.R;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private List<Movie> mMovieList;

    private MovieListener mMovieListener;

    public interface MovieListener {
        void movieSelected(int position, Movie movie, View viewToAnimate);
    }

    public MovieAdapter(Context context, List<Movie> movieList, MovieListener movieListener) {
        mContext = context;
        mMovieList = movieList;
        mMovieListener = movieListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_adapter, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie movie = mMovieList.get(position);

        if (movie != null) {

            Picasso.with(mContext)
                    .load(BuildImageUrl(movie.getBackdropPath()))
                    .error(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.posterImageView);


            if (movie.getTitle() != null && !TextUtils.isEmpty(movie.getTitle())) {
                holder.titleTextView.setText(movie.getTitle());
            }

            ViewCompat.setTransitionName(holder.posterImageView, movie.getTitle());

            holder.wrapperMovieRelativeLayout.setOnClickListener(v -> mMovieListener.movieSelected(position, movie, holder.posterImageView));
        }

    }

    private String BuildImageUrl(String endPointUrl) {
        return Constants.BASE_IMAGE_URL + Constants.IMAGE_QUALITY_W500 + endPointUrl;
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_movie)
        ImageView posterImageView;
        @BindView(R.id.tv_title)
        TextView titleTextView;
        @BindView(R.id.rl_wrapper_movie)
        RelativeLayout wrapperMovieRelativeLayout;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
