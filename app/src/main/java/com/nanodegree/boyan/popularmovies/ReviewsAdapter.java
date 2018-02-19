package com.nanodegree.boyan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.Movie;
import com.nanodegree.boyan.popularmovies.data.Review;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private List<Review> mReviewData;

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorTextView;
        public final TextView mContentTextView;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.author);
            mContentTextView = view.findViewById(R.id.content);
        }
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder reviewsAdapterViewHolder, int position) {
        Review review = mReviewData.get(position);
        reviewsAdapterViewHolder.mAuthorTextView.setText(String.format("%s:", review.getAuthor()));
        reviewsAdapterViewHolder.mContentTextView.setText(review.getContent());
    }


    @Override
    public int getItemCount() {
        if (null == mReviewData)
            return 0;

        return mReviewData.size();
    }

    public void setReviewsData(List<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}