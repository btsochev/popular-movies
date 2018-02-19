package com.nanodegree.boyan.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {


    public static final String CONTENT_AUTHORITY = "com.nanodegree.boyan.popularmovies";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static  Uri buildUri(String movieId){
            return CONTENT_URI.buildUpon()
                    .appendPath(movieId)
                    .build();
        }

        public static final String TABLE_NAME = "movie";


        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";

    }
}