package com.nanodegree.boyan.popularmovies.utilities;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private static final String SORT = "sort_by";

    private static final String SHARED_PREFS_KEY = "popular_movies_shared_preferred";


    private static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }


    private static void setInt(Context context, String key, int value) {
        SharedPreferences sp = getInstance(context);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, value);
        e.apply();
    }

    private static int getInt(Context context, String key) {
        SharedPreferences sp = getInstance(context);
        return sp.getInt(key, 1);
    }


    public static int getSort(Context context) {
        return getInt(context, SORT);
    }

    public static void setSort(Context context, int value) {
        setInt(context, SORT, value);
    }
}
