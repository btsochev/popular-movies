package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;

import org.json.JSONObject;

public class ProductionCompany implements IJsonDeserialize {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        id = jsonObject.optInt("id");
        name = jsonObject.optString("name");
    }
}
