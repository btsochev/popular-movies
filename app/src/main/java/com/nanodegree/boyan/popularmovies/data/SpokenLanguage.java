package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;

import org.json.JSONObject;

public class SpokenLanguage implements IJsonDeserialize {
    private String iso6391;
    private String name;

    public String getIso6391() {
        return iso6391;
    }

    public String getName() {
        return name;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        name = jsonObject.optString("name");
        iso6391 = jsonObject.optString("iso_639_1");
    }
}
