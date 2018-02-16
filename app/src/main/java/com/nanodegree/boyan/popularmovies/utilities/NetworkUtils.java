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
package com.nanodegree.boyan.popularmovies.utilities;

import android.net.Uri;

import com.nanodegree.boyan.popularmovies.data.MovieDetails;
import com.nanodegree.boyan.popularmovies.data.MovieResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {
    private static final String BASE_IMAGE_URL = " http://image.tmdb.org/t/p/w185/";
    private static final String POPULAR_MOVIES_URL = "movie/popular";
    private static final String TOP_RATED_MOVIES_URL = "movie/top_rated";
    private static final String MOVIE_DETAILS = "movie";

    private static final String BASE_URL = "https://api.themoviedb.org/3";


    //TODO: remove before commit and read it from strings
    final static String AUTH_PARAM = "api_key";

    public static URL buildUrl(String appendPath, String apiKey) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(appendPath)
                .appendQueryParameter(AUTH_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildPosterUrl(String imagePath) {
        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieDetailsrUrl(int movieId, String apiKey) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(MOVIE_DETAILS)
                .appendEncodedPath(String.valueOf(movieId))
                .appendQueryParameter(AUTH_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static MovieResponse getPopularMovies(String apiKey) throws Exception {
        URL popularMoviesUrl = buildUrl(POPULAR_MOVIES_URL, apiKey);
        String stringResponse = getResponseFromHttpUrl(popularMoviesUrl);
        return parseMovieResponse(stringResponse);
    }

    public static MovieResponse getTopRatedMovies(String apiKey) throws Exception {
        URL topRatedMoviesUrl = buildUrl(TOP_RATED_MOVIES_URL, apiKey);
        String stringResponse = getResponseFromHttpUrl(topRatedMoviesUrl);
        return parseMovieResponse(stringResponse);
    }

    public static MovieDetails getMovieDetails(int movieId, String apiKey) throws Exception {
        URL movieDetails = buildMovieDetailsrUrl(movieId, apiKey);
        String stringResponse = getResponseFromHttpUrl(movieDetails);
        return parseMovieDetails(stringResponse);
    }

    public static MovieResponse parseMovieResponse(String response) throws JSONException {
        if (StringHelpers.isStringNullOrEmpty(response))
            return null;

        JSONObject jsonResponse = new JSONObject(response);
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.getByJsonObject(jsonResponse);
        return movieResponse;
    }

    public static MovieDetails parseMovieDetails(String response) throws JSONException {
        if (StringHelpers.isStringNullOrEmpty(response))
            return null;

        JSONObject jsonResponse = new JSONObject(response);
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.getByJsonObject(jsonResponse);
        return movieDetails;
    }

    public static String getResponseFromHttpUrl(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(2000);
            urlConnection.setReadTimeout(2000);

            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200)
                throw new Exception("Http Error: Status Code - " + statusCode);

            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}