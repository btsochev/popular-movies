package com.nanodegree.boyan.popularmovies;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nanodegree.boyan.popularmovies.data.MoviesContract;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIES_LOADER_ID = 10;

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;

    private FavoritesAdapter adapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final String[] MAIN_FAVORITES_PROJECTION = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mRecyclerView = findViewById(R.id.favorites_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        adapter = new FavoritesAdapter(this, this::onMovieClicked);
        mRecyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);

        setTitle(getResources().getString(R.string.favorites));
    }

    private void onMovieClicked(int movieId) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movie_id", movieId);
        startActivity(intent);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri favoritesUri = MoviesContract.MovieEntry.CONTENT_URI;

        return new CursorLoader(this,
                favoritesUri,
                MAIN_FAVORITES_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
