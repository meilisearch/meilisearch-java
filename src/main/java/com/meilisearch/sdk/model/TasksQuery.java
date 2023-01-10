package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of a query parameter for tasks route
 *
 * <p>https://docs.meilisearch.com/reference/api/tasks.html#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class TasksQuery {
    private int limit = -1;
    private int from = -1;
    private String[] status;
    private String[] type;
    private String[] indexUid;

    public TasksQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("from", this.getFrom())
                        .addParameter("status", this.getStatus())
                        .addParameter("type", this.getType())
                        .addParameter("indexUid", this.getIndexUid());
        return urlb.getURL();
    }
}
