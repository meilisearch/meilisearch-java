package com.meilisearch.sdk;

import com.google.gson.Gson;

import lombok.Getter;

import java.util.Date;

/** MeiliSearch response for a Task */
public class Task {
    @Getter protected String status = "";
    @Getter protected int uid = 0;
    @Getter protected String indexUid = "";
    @Getter protected String type = null;
    @Getter protected String duration = "";
    @Getter protected Date enqueuedAt = null;
    @Getter protected Date startedAt = null;
    @Getter protected Date finishedAt = null;
    @Getter protected TaskError error = null;
    @Getter protected Details details = null;

    private static Gson gsonTask = new Gson();

    /**
     * Method to return the JSON String of the Task
     *
     * @return JSON string of the Task object
     */
    @Override
    public String toString() {
        return gsonTask.toJson(this);
    }
}
