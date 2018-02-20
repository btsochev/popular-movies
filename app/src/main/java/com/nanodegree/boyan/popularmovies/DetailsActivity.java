package com.nanodegree.boyan.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.boyan.popularmovies.data.MovieDetails;
import com.nanodegree.boyan.popularmovies.data.MovieDetailsWrapper;
import com.nanodegree.boyan.popularmovies.database.MoviesContract;
import com.nanodegree.boyan.popularmovies.data.Review;
import com.nanodegree.boyan.popularmovies.data.Video;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetailsWrapper> {
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private ImageView posterIv;
    private TextView titleTv;
    private TextView overviewTv;
    private TextView userRatingTv;
    private TextView releaseDateTv;

    private TextView reviewsLabel;
    private TextView trailersLabel;

    private Button favBtn;

    private NestedScrollView mScrollView;
    private RecyclerView reviewsRv;
    private RecyclerView trailersRv;

    ReviewsAdapter reviewsAdapter;
    TrailersAdapter trailersAdapter;

    private static final String MOVIE_ID_PARAM = "movie_id";
    private static final int MOVIE_DETAILS_LOADER_ID = 1;

    private boolean isFavoriteFlag = false;
    private int movieId;
    private MovieDetails movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie_id")) {
                movieId = intentThatStartedThisActivity.getIntExtra("movie_id", -1);
            }
        }

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        posterIv = findViewById(R.id.poster_iv);
        titleTv = findViewById(R.id.title_tv);
        overviewTv = findViewById(R.id.overview_tv);
        userRatingTv = findViewById(R.id.user_rating_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);

        favBtn = findViewById(R.id.fav_btn);
        favBtn.setOnClickListener(onFavButtonClickListner);

        reviewsLabel = findViewById(R.id.reviews_label);
        trailersLabel = findViewById(R.id.trailers_label);

        mScrollView = findViewById(R.id.scrollView);
        reviewsRv = findViewById(R.id.reviews_rv);
        trailersRv = findViewById(R.id.trailers_rv);
        trailersRv.setHasFixedSize(true);

        LinearLayoutManager reviewLayoutManger = new LinearLayoutManager(this);
        reviewsRv.setLayoutManager(reviewLayoutManger);
        reviewsAdapter = new ReviewsAdapter();
        reviewsRv.setAdapter(reviewsAdapter);

        LinearLayoutManager trailersLayoutManger = new LinearLayoutManager(this);
        trailersRv.setLayoutManager(trailersLayoutManger);
        trailersAdapter = new TrailersAdapter(this::onVideoClicked);
        trailersRv.setAdapter(trailersAdapter);

        LoaderManager.LoaderCallbacks<MovieDetailsWrapper> callback = DetailsActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putInt(MOVIE_ID_PARAM, movieId);

        getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_ID, bundleForLoader, callback);
        isFavoriteFlag = isFavorite(movieId);
    }

    @Override
    public Loader<MovieDetailsWrapper> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDetailsWrapper>(this) {
            MovieDetailsWrapper mData = null;

            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieDetailsWrapper loadInBackground() {

                try {
                    int movieId = args.getInt(MOVIE_ID_PARAM);
                    MovieDetails movieDetails = NetworkUtils.getMovieDetails(movieId, getResources().getString(R.string.api_key));
                    List<Review> reviews = NetworkUtils.getMovieReviews(movieId, getResources().getString(R.string.api_key));
                    List<Video> videos = NetworkUtils.getMovieTrailers(movieId, getResources().getString(R.string.api_key));

                    MovieDetailsWrapper movieDetailsWrapper = new MovieDetailsWrapper();
                    movieDetailsWrapper.setMovieDetails(movieDetails);
                    movieDetailsWrapper.setReviews(reviews);
                    movieDetailsWrapper.setVideos(videos);

                    return movieDetailsWrapper;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(MovieDetailsWrapper data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieDetailsWrapper> loader, MovieDetailsWrapper data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
        } else {
            this.movieDetails = data.getMovieDetails();
            showMovieDetailsView();
            populateData(data);

            reviewsAdapter.setReviewsData(data.getReviews());
            trailersAdapter.setTrailersData(data.getVideos());
        }

    }

    @Override
    public void onLoaderReset(Loader<MovieDetailsWrapper> loader) {

    }

    private void onVideoClicked(Video video) {
        if (video.getId() == null || video.getId().isEmpty()) {
            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        startActivity(webIntent);
    }

    private void populateData(MovieDetailsWrapper data) {
        URL urlPoster = NetworkUtils.buildImageUrl(data.getMovieDetails().getPosterPath());
        Picasso.with(this).load(urlPoster.toString()).into(posterIv);

        titleTv.setText(data.getMovieDetails().getOriginalTitle());
        overviewTv.setText(data.getMovieDetails().getOverview());
        userRatingTv.setText(String.valueOf(data.getMovieDetails().getVoteCount()));
        releaseDateTv.setText(data.getMovieDetails().getReleaseDate());

        if (data.getReviews() == null || data.getReviews().size() == 0)
            reviewsLabel.setVisibility(View.GONE);

        if (data.getVideos() == null || data.getVideos().size() == 0)
            trailersLabel.setVisibility(View.GONE);

        updateFavoritesView();

    }

    private void updateFavoritesView() {
        if (isFavoriteFlag) {
            favBtn.setText(getResources().getText(R.string.remove_from_favorites));
            favBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_star_white_24dp), null, null);
        } else {
            favBtn.setText(getResources().getText(R.string.add_to_favorites));
            favBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_star_border_white_24dp), null, null);
        }
    }

    private View.OnClickListener onFavButtonClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isFavoriteFlag) {
                int deletedMovie = getContentResolver().delete(MoviesContract.MovieEntry.buildUri(String.valueOf(movieId)), null, new String[]{String.valueOf(movieId)});
                if (deletedMovie > 0) {
                    isFavoriteFlag = false;
                    updateFavoritesView();
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
                contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, movieDetails.getTitle());
                contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movieDetails.getPosterPath());
                Uri insertUri =  getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValues);
                if (insertUri != null){
                    isFavoriteFlag = true;
                    updateFavoritesView();
                }
            }
        }
    };

    private boolean isFavorite(int movieId) {
        Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.buildUri(String.valueOf(movieId)), null, null, new String[]{String.valueOf(movieId)}, null);
        if (cursor != null && cursor.getCount() > 0)
            return true;

        return false;
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
