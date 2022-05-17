package com.meilisearch.sdk;

import com.google.gson.Gson;
import lombok.Getter;

/** MeiliSearch response for a Result */
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
