package com.meilisearch.sdk.model;

import java.util.Date;
import lombok.Getter;

/**
 * Data structure of Meilisearch response for a Task
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks">API specification</a>
 */
@Getter
public class Task {
    protected TaskStatus status = null;
    protected int uid = 0;
    protected String indexUid = "";
    protected String type = null;
    protected String duration = "";
    protected Date enqueuedAt = null;
    protected Date startedAt = null;
    protected Date finishedAt = null;
    protected TaskError error = null;
    protected TaskDetails details = null;
    protected String customMetadata = null;

    public Task() {}
}
