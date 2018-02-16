package com.nanodegree.boyan.popularmovies.utilities;


public class StringHelpers {

    public static boolean isStringNullOrEmpty(String value) {
        if (value == null || value.isEmpty())
            return true;

        return false;
    }
}
