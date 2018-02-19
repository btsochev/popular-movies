package com.nanodegree.boyan.popularmovies.utilities;

import android.net.Uri;

import com.nanodegree.boyan.popularmovies.data.MovieDetails;
import com.nanodegree.boyan.popularmovies.data.MovieResponse;
import com.nanodegree.boyan.popularmovies.data.Review;
import com.nanodegree.boyan.popularmovies.data.ReviewsResponse;
import com.nanodegree.boyan.popularmovies.data.Video;
import com.nanodegree.boyan.popularmovies.data.VideosResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;


public final class NetworkUtils {
    private static final String BASE_IMAGE_URL = " http://image.tmdb.org/t/p/w185/";
    private static final String POPULAR_MOVIES_URL = "movie/popular";
    private static final String TOP_RATED_MOVIES_URL = "movie/top_rated";
    private static final String MOVIE_DETAILS = "movie";

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private final static String AUTH_PARAM = "api_key";

    public static URL buildImageUrl(String imagePath) {
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

    private static URL buildUrl(String apiKey, String... pathComponents) {
        Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon();

        for (String pathComponent : pathComponents) {
            uriBuilder.appendEncodedPath(pathComponent);
        }

        uriBuilder = uriBuilder.appendQueryParameter(AUTH_PARAM, apiKey);

        Uri builtUri = uriBuilder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static MovieResponse getPopularMovies(String apiKey) throws Exception {
        URL popularMoviesUrl = buildUrl(apiKey, POPULAR_MOVIES_URL);
        String stringResponse = getResponseFromHttpUrl(popularMoviesUrl);
        return parseResponse(stringResponse, MovieResponse.class);
    }

    public static MovieResponse getTopRatedMovies(String apiKey) throws Exception {
        URL topRatedMoviesUrl = buildUrl(apiKey, TOP_RATED_MOVIES_URL);
        String stringResponse = getResponseFromHttpUrl(topRatedMoviesUrl);
        return parseResponse(stringResponse, MovieResponse.class);
    }

    public static MovieDetails getMovieDetails(int movieId, String apiKey) throws Exception {
        URL movieDetails = buildUrl(apiKey, MOVIE_DETAILS, String.valueOf(movieId));
        String stringResponse = getResponseFromHttpUrl(movieDetails);
        return parseResponse(stringResponse, MovieDetails.class);
    }

    public static List<Review> getMovieReviews(int movieId, String apiKey) throws Exception {
        URL movieDetails = buildUrl(apiKey, MOVIE_DETAILS, String.valueOf(movieId), "reviews");
        String stringResponse = getResponseFromHttpUrl(movieDetails);
        ReviewsResponse reviewsResponse = parseResponse(stringResponse, ReviewsResponse.class);
        return reviewsResponse.getResults();
    }

    public static List<Video> getMovieTrailers(int movieId, String apiKey) throws Exception {
        URL movieDetails = buildUrl(apiKey, MOVIE_DETAILS, String.valueOf(movieId), "videos");
        String stringResponse = getResponseFromHttpUrl(movieDetails);
        VideosResponse videosResponse = parseResponse(stringResponse, VideosResponse.class);
        return videosResponse.getResults();
    }

    public static <T extends IJsonDeserialize> T parseResponse(String response, Class<T> classInstance) {
        if (StringHelpers.isStringNullOrEmpty(response))
            return null;

        T responseObject = null;
        try {
            JSONObject jsonResponse = new JSONObject(response);
            responseObject = classInstance.newInstance();
            responseObject.getByJsonObject(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseObject;
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