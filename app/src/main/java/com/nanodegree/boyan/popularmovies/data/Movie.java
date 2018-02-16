package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;
import com.nanodegree.boyan.popularmovies.utilities.JsonHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Movie implements IJsonDeserialize {
    private Double voteAverage;
    private String backdropPath;
    private boolean adult;
    private Integer id;
    private String title;
    private String overview;
    private String originalLanguage;
    private List<Integer> genreIds;
    private String releaseDate;
    private String originalTitle;
    private Integer voteCount;
    private String posterPath;
    private boolean video;
    private Double popularity;

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        try {
            voteCount = jsonObject.optInt("vote_count", 0);
            id = jsonObject.optInt("id", 0);
            video = jsonObject.optBoolean("video");
            voteAverage = jsonObject.optDouble("vote_average", 0);
            title = jsonObject.optString("title");
            popularity = jsonObject.optDouble("popularity", 0);
            posterPath = jsonObject.optString("poster_path");
            originalLanguage = jsonObject.optString("original_language");
            originalTitle = jsonObject.optString("original_title");
            genreIds = JsonHelpers.getIntegerList(jsonObject, "genre_ids");
            backdropPath = jsonObject.optString("backdrop_path");
            adult = jsonObject.optBoolean("adult");
            overview = jsonObject.optString("overview");
            releaseDate = jsonObject.optString("release_date");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
