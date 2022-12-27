package com.meilisearch.sdk.model;

import lombok.Getter;

/**
 * Data structure of Meilisearch response for tasks Results
 *
 * <p>https://docs.meilisearch.com/reference/api/tasks.html#response
 */
@Getter
public class TasksResults {
    protected Task[] results = null;
    protected int limit;
    protected int from;
    protected int next;

    public TasksResults() {}
}
