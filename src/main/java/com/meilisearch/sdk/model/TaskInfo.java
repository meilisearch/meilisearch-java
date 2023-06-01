package com.meilisearch.sdk.model;

import java.util.Date;
import lombok.Getter;

/**
 * Data structure of Meilisearch response for a asynchronous operation
 *
 * <p>https://www.meilisearch.com/docs/reference/api/tasks
 */
@Getter
public class TaskInfo {
    protected TaskStatus status = null;
    protected int taskUid = 0;
    protected String indexUid = "";
    protected String type = null;
    protected Date enqueuedAt = null;

    public TaskInfo() {}
}
