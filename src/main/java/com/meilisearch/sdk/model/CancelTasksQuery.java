package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of a query parameter for tasks route
 *
 * <p>https://www.meilisearch.com/docs/reference/api/tasks#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class CancelTasksQuery {
    private int[] uids;
    private String[] statuses;
    private String[] types;
    private String[] indexUids;
    private Date beforeEnqueuedAt;
    private Date afterEnqueuedAt;
    private Date beforeStartedAt;
    private Date afterStartedAt;

    public CancelTasksQuery() {}

    public CancelTasksQuery setUids(int... uids) {
        this.uids = uids;
        return this;
    }

    public CancelTasksQuery setStatuses(String... statuses) {
        this.statuses = statuses;
        return this;
    }

    public CancelTasksQuery setTypes(String... types) {
        this.types = types;
        return this;
    }

    public CancelTasksQuery setIndexUids(String... indexUids) {
        this.indexUids = indexUids;
        return this;
    }

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("uids", this.getUids())
                        .addParameter("statuses", this.getStatuses())
                        .addParameter("types", this.getTypes())
                        .addParameter("indexUids", this.getIndexUids())
                        .addParameter("beforeEnqueuedAt", this.getBeforeEnqueuedAt())
                        .addParameter("afterEnqueuedAt", this.getAfterEnqueuedAt())
                        .addParameter("beforeStartedAt", this.getBeforeStartedAt())
                        .addParameter("afterStartedAt", this.getAfterStartedAt());
        return urlb.getURL();
    }
}
