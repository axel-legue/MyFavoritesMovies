package com.legue.axel.myfavoritesmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legue.axel.myfavoritesmovies.R;
import com.legue.axel.myfavoritesmovies.library.Constants;
import com.legue.axel.myfavoritesmovies.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private final static String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Trailer> mTrailerList;
    private TrailerListener mTrailerListener;

    public interface TrailerListener {
        void onTrailerSelected(Intent intent);
    }

    public TrailerAdapter(Context context, List<Trailer> trailerList, TrailerListener trailerListener) {
        mContext = context;
        mTrailerList = trailerList;
        mTrailerListener = trailerListener;
    }

    @NonNull
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_adapter, parent, false);
        return new TrailerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        final Trailer trailer = mTrailerList.get(position);

        if (trailer != null) {
            holder.mCardViewTrailer.setOnClickListener(v -> mTrailerListener.onTrailerSelected(buildYoutubeIntent(trailer.getKey())));
            String trailerName = mContext.getString(R.string.trailer_name, (position + 1));
            holder.mTrailerName.setText(trailerName);
        }
    }

    private Intent buildYoutubeIntent(String key) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_YOUTUBE_URL + key));
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_trailer)
        CardView mCardViewTrailer;
        @BindView(R.id.iv_ic_play)
        ImageView mPlayIcon;
        @BindView(R.id.tv_trailer_name)
        TextView mTrailerName;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
