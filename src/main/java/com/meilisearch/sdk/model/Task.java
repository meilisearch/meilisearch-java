package com.meilisearch.sdk.model;

import com.google.gson.Gson;
import com.meilisearch.sdk.TaskError;
import java.util.Date;
import lombok.Getter;

/**
 * Data structure of MeiliSearch response for a Task
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/tasks.html
 */
@Getter
public class Task {
    protected String status = "";
    protected int uid = 0;
    protected String indexUid = "";
    protected String type = null;
    protected String duration = "";
    protected Date enqueuedAt = null;
    protected Date startedAt = null;
    protected Date finishedAt = null;
    protected TaskError error = null;
    protected Details details = null;

    private static Gson gsonTask = new Gson();

    public Task() {}

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
