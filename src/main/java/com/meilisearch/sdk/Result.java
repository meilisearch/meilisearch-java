package com.meilisearch.sdk;

import com.google.gson.Gson;

/** MeiliSearch response for a Result */
public class Result<T> {
    protected T[] results = null;

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

    /**
     * Method to return the list contained in Result
     *
     * @return String containing the identifier of the Task
     */
    public T[] getResults() {
        return this.results;
    }
}
