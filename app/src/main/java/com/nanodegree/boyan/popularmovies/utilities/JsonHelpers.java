package com.nanodegree.boyan.popularmovies.utilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelpers {
    public static List<Integer> getIntegerList(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.has(name) || jsonObject.isNull(name))
            return null;

        List<Integer> result = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray(name);
        if (jsonArray == null)
            return null;

        for (int i = 0; i < jsonArray.length(); i++) {
            Integer number = jsonArray.optInt(i);
            result.add(number);
        }

        return result;
    }

    public static <T extends IJsonDeserialize> ArrayList<T> optArrayList(JSONObject jsonObject, String name, Class<T> cls) {
        JSONArray propertyJsonArray = jsonObject.optJSONArray(name);
        if (propertyJsonArray == null)
            return null;

        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < propertyJsonArray.length(); i++) {
            JSONObject itemJsonObject = propertyJsonArray.optJSONObject(i);
            if (itemJsonObject == null) {
                arrayList.add(null);
                continue;
            }

            T obj = null;
            try {
                obj = cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (obj == null) {
                arrayList.add(null);
                continue;
            }

            obj.getByJsonObject(itemJsonObject);
            arrayList.add(obj);
        }
        return arrayList;
    }
}
