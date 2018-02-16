package com.nanodegree.boyan.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.MovieDetails;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails> {
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private ImageView posterIv;
    private ImageView backdropIv;
    private TextView titleTv;
    private TextView overviewTv;
    private TextView userRatingTv;
    private TextView releaseDateTv;

    private ScrollView mScrollView;

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

        posterIv = findViewById(R.id.poster_iv);
        backdropIv = findViewById(R.id.backdrop_iv);
        titleTv = findViewById(R.id.title_tv);
        overviewTv = findViewById(R.id.overview_tv);
        userRatingTv = findViewById(R.id.user_rating_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);

        mScrollView = findViewById(R.id.scrollView);

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
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDetailsView();
            populateData(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<MovieDetails> loader) {

    }

    private void populateData(MovieDetails movieDetails) {
        URL urlPoster = NetworkUtils.buildImageUrl(movieDetails.getPosterPath());
        Picasso.with(this).load(urlPoster.toString()).into(posterIv);

        URL urlBackdrop = NetworkUtils.buildImageUrl(movieDetails.getBackdropPath());
        Picasso.with(this).load(urlBackdrop.toString()).into(backdropIv);

        titleTv.setText(movieDetails.getOriginalTitle());
        overviewTv.setText(movieDetails.getOverview());
        userRatingTv.setText(String.valueOf(movieDetails.getVoteCount()));
        releaseDateTv.setText(movieDetails.getReleaseDate());
    }

    private void showErrorMessage() {
        mScrollView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMovieDetailsView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
    }
}
