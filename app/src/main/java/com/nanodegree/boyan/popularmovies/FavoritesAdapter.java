package com.nanodegree.boyan.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {
    private Cursor mCursor;
    private final Context mContext;
    private ForecastAdapterOnClickHandler mClickHandler;

    @Override
    public FavoritesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.favorites_list_item, parent, false);

        view.setFocusable(true);

        return new FavoritesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = mCursor.getString(FavoritesActivity.INDEX_MOVIE_TITLE);
        holder.mTitleTextView.setText(title);
        holder.movieId = mCursor.getInt(FavoritesActivity.INDEX_MOVIE_ID);
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
        public final ImageView mIconImaveView;
        public final TextView mTitleTextView;

        private int movieId;

        public FavoritesAdapterViewHolder(View view) {
            super(view);
            mIconImaveView = view.findViewById(R.id.icon);
            mTitleTextView = view.findViewById(R.id.title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(movieId);
        }
    }
}
