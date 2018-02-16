package com.nanodegree.boyan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.Movie;
import com.nanodegree.boyan.popularmovies.data.MovieResponse;
import com.nanodegree.boyan.popularmovies.data.MoviesSortType;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.nanodegree.boyan.popularmovies.utilities.PreferencesHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieResponse>, MoviesAdapter.MoviesAdapterOnClickHandler {

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private static final int MOVIES_LOADER_ID = 0;
    private static final String SORT_PARAM = "sort_param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        int numberOfColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        LoaderManager.LoaderCallbacks<MovieResponse> callback = MainActivity.this;
        int sort = PreferencesHelper.getSort(this);
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putInt(SORT_PARAM, sort);

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, bundleForLoader, callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_sort_popular) {
            PreferencesHelper.setSort(MainActivity.this, MoviesSortType.POPULAR);
            refreshData(MoviesSortType.POPULAR);
            return true;
        }

        if (itemId == R.id.action_sort_top_rated) {
            PreferencesHelper.setSort(MainActivity.this, MoviesSortType.TOP_RATED);
            refreshData(MoviesSortType.TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData(int sort) {
        mMoviesAdapter.setMoviesData(null);
        Bundle bundle = new Bundle();
        bundle.putInt(SORT_PARAM, sort);
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundle, this);
    }

    @Override
    public Loader<MovieResponse> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieResponse>(this) {
            MovieResponse mMoviesData = null;
            int forSort = MoviesSortType.POPULAR;

            @Override
            protected void onStartLoading() {
                if (mMoviesData != null && forSort == args.getInt(SORT_PARAM)) {
                    deliverResult(mMoviesData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieResponse loadInBackground() {

                try {
                    switch (args.getInt(SORT_PARAM)) {
                        case MoviesSortType.POPULAR:
                            return NetworkUtils.getPopularMovies(getResources().getString(R.string.api_key));
                        case MoviesSortType.TOP_RATED:
                            return NetworkUtils.getTopRatedMovies(getResources().getString(R.string.api_key));
                        default:
                            return null;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(MovieResponse data) {
                forSort = args.getInt(SORT_PARAM);
                mMoviesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieResponse> loader, MovieResponse data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
        } else {
            showMoviesDataView();
            mMoviesAdapter.setMoviesData(data.getResults());
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieResponse> loader) {

    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }
}
