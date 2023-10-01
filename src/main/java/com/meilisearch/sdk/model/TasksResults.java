package com.meilisearch.sdk.model;

import lombok.Getter;

/**
 * Data structure of Meilisearch response for tasks Results
 *
 * <p><a href="https://www.meilisearch.com/docs/reference/api/tasks#response">https://www.meilisearch.com/docs/reference/api/tasks#response</a>
 */
@Getter
public class TasksResults {
    protected Task[] results = null;
    protected int limit;
    protected int from;
    protected int next;
    protected int total;

    public TasksResults() {}
}
