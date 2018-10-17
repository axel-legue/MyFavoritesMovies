package com.legue.axel.myfavoritesmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legue.axel.myfavoritesmovies.R;
import com.legue.axel.myfavoritesmovies.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_adapter, parent, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        final Review review = mReviewList.get(position);

        if (review != null) {
            String author = mContext.getString(R.string.author_name, review.getAuthor());
            holder.mAuthor.setText(author);
            holder.mReview.setText(review.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author)
        TextView mAuthor;
        @BindView(R.id.tv_review)
        TextView mReview;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
