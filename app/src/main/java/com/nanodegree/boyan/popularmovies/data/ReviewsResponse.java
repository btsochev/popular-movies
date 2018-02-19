package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewsResponse implements IJsonDeserialize {
    private int id;
    private int page;
    private int totalResults;
    private int totalPages;
    private List<Review> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        id = jsonObject.optInt("id");
        page = jsonObject.optInt("page");
        totalResults = jsonObject.optInt("total_results");
        totalPages = jsonObject.optInt("total_pages");

        results = new ArrayList<>();
        JSONArray resultsJsonArray = jsonObject.optJSONArray("results");
        if (resultsJsonArray == null || resultsJsonArray.length() == 0)
            return;

        for (int i = 0; i < resultsJsonArray.length(); i++) {
            JSONObject resultJsonObject = resultsJsonArray.optJSONObject(i);
            if (resultJsonObject == null)
                continue;

            Review review = new Review();
            review.getByJsonObject(resultJsonObject);

            results.add(review);
        }
    }
}
