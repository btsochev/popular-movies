package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse implements IJsonDeserialize {
    private Integer page;
    private List<Movie> results;
    private Integer totalResults;
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        try {
            page = jsonObject.optInt("page", 0);
            totalResults = jsonObject.optInt("total_results", 0);
            totalPages = jsonObject.optInt("total_pages", 0);
            ArrayList<Movie> moviesList = new ArrayList<>();
            JSONArray moviesJsonArray = jsonObject.getJSONArray("results");
            if (moviesJsonArray != null) {
                for (int i = 0; i < moviesJsonArray.length(); i++) {
                    JSONObject movieJsonObject = moviesJsonArray.optJSONObject(i);
                    if (movieJsonObject != null) {
                        Movie movie = new Movie();
                        movie.getByJsonObject(movieJsonObject);
                        moviesList.add(movie);
                    }
                }
            }
            results = moviesList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
