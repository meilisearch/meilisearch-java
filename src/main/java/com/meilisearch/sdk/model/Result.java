package com.meilisearch.sdk.model;

import com.google.gson.Gson;
import lombok.Getter;

/** Data structure of MeiliSearch response for a Result */
public class Result<T> {
    @Getter protected T[] results = null;

    private static Gson gsonResult = new Gson();

    /**
     * Method to return the JSON String of the Result
     *
     * @return JSON string of the Result object
     */
    @Override
    public String toString() {
        return gsonResult.toJson(this);
    }
}
