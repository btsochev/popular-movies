package com.nanodegree.boyan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.Review;
import com.nanodegree.boyan.popularmovies.data.Video;

import java.util.List;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private List<Video> mTrailerData;
    private TrailersAdapterOnClickHandler mClickHandler;

    public interface TrailersAdapterOnClickHandler {
        void onClick(Video video);
    }

    public TrailersAdapter(TrailersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mIconImaveView;
        public final TextView mNameTextView;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            mIconImaveView = view.findViewById(R.id.icon);
            mNameTextView = view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Video video = mTrailerData.get(adapterPosition);
            mClickHandler.onClick(video);
        }
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder trailersAdapterViewHolder, int position) {
        Video video = mTrailerData.get(position);
        trailersAdapterViewHolder.mNameTextView.setText(video.getName());
    }


    @Override
    public int getItemCount() {
        if (null == mTrailerData)
            return 0;

        return mTrailerData.size();
    }

    public void setTrailersData(List<Video> videoData) {
        mTrailerData = videoData;
        notifyDataSetChanged();
    }
}