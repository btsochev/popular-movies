/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nanodegree.boyan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.boyan.popularmovies.data.Movie;
import com.nanodegree.boyan.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMovieData;

    final private MoviesAdapterOnClickHandler mClickHandler;


    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView mMovieImageView;
        public final TextView mMovieTitleTextView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.image);
            mMovieTitleTextView = view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        Movie movie = mMovieData.get(position);
        URL url = NetworkUtils.buildPosterUrl(movie.getPosterPath());
        Picasso.with(moviesAdapterViewHolder.mMovieImageView.getContext()).load(url.toString()).into(moviesAdapterViewHolder.mMovieImageView);
        moviesAdapterViewHolder.mMovieTitleTextView.setText(movie.getTitle());
    }


    @Override
    public int getItemCount() {
        if (null == mMovieData)
            return 0;

        return mMovieData.size();
    }

    public void setMoviesData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}