package com.meilisearch.sdk.model;

import java.util.Date;
import lombok.Getter;

/**
 * Data structure of Meilisearch response for a asynchronous operation
 *
 * <p>https://docs.meilisearch.com/reference/api/tasks.html
 */
@Getter
public class TaskInfo {
    protected String status = "";
    protected int taskUid = 0;
    protected String indexUid = "";
    protected String type = null;
    protected Date enqueuedAt = null;

    public TaskInfo() {}
}
