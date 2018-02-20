package com.nanodegree.boyan.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {
    private Cursor mCursor;
    private final Context mContext;
    private ForecastAdapterOnClickHandler mClickHandler;

    @Override
    public FavoritesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_grid_item, parent, false);

        view.setFocusable(true);

        return new FavoritesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = mCursor.getString(FavoritesActivity.INDEX_MOVIE_TITLE);
        String posterPath = mCursor.getString(FavoritesActivity.INDEX_MOVIE_POSTER_PATH);
        int movieId = mCursor.getInt(FavoritesActivity.INDEX_MOVIE_ID);

        holder.mMovieTitleTextView.setText(title);
        holder.movieId = movieId;
        URL url = NetworkUtils.buildImageUrl(posterPath);
        Picasso.with(holder.mMovieImageView.getContext()).load(url.toString()).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface ForecastAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public FavoritesAdapter (Context context, ForecastAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;
        public final TextView mMovieTitleTextView;

        private int movieId;

        public FavoritesAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.image);
            mMovieTitleTextView = view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(movieId);
        }
    }
}
