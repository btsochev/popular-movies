package com.nanodegree.boyan.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.MovieDetails;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails> {
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private static final String MOVIE_ID_PARAM = "movie_id";
    private static final int MOVIE_DETAILS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int movieId = -1;
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie_id")) {
                movieId = intentThatStartedThisActivity.getIntExtra("movie_id", -1);
            }
        }

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);


        LoaderManager.LoaderCallbacks<MovieDetails> callback = DetailsActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putInt(MOVIE_ID_PARAM, movieId);

        getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, bundleForLoader, callback);
    }

    @Override
    public Loader<MovieDetails> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDetails>(this) {
            MovieDetails mMovieDetails = null;

            @Override
            protected void onStartLoading() {
                if (mMovieDetails != null) {
                    deliverResult(mMovieDetails);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieDetails loadInBackground() {

                try {
                   int movieId = args.getInt(MOVIE_ID_PARAM);
                   return NetworkUtils.getMovieDetails(movieId, getResources().getString(R.string.api_key));

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(MovieDetails data) {
                mMovieDetails = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails data) {
        MovieDetails movieDetails = data;
    }

    @Override
    public void onLoaderReset(Loader<MovieDetails> loader) {

    }
}
