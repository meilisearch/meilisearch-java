package com.meilisearch.sdk;

import com.google.gson.Gson;

/** MeiliSearch response for an Task */
public class Result {
    protected Task[] results = null;

    private static Gson gsonUpdate = new Gson();

    /**
     * Method to return the JSON String of the Result
     *
     * @return JSON string of the Result object
     */
    @Override
    public String toString() {
        return gsonUpdate.toJson(this);
    }

    /**
     * Method to return the uid of the T
     *
     * @return String containing the identifier of the Task
     */
    public Task[] getResults() {
        return this.results;
    }
}
