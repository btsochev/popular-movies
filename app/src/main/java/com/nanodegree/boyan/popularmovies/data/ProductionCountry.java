package com.nanodegree.boyan.popularmovies.data;


import com.nanodegree.boyan.popularmovies.utilities.IJsonDeserialize;

import org.json.JSONObject;

public class ProductionCountry implements IJsonDeserialize {
    private String name;
    private String iso31661;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    @Override
    public void getByJsonObject(JSONObject jsonObject) {
        name = jsonObject.optString("name");
        iso31661 = jsonObject.optString("iso_3166_1");
    }
}
