package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import java.util.Date;
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
public class DeleteTasksQuery {
    private int[] uids;
    private String[] statuses;
    private String[] types;
    private String[] indexUids;
    private int[] canceledBy;
    private Date beforeEnqueuedAt;
    private Date afterEnqueuedAt;
    private Date beforeStartedAt;
    private Date afterStartedAt;
    private Date beforeFinishedAt;
    private Date afterFinishedAt;

    public DeleteTasksQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("uids", this.getUids())
                        .addParameter("statuses", this.getStatuses())
                        .addParameter("types", this.getTypes())
                        .addParameter("indexUids", this.getIndexUids())
                        .addParameter("canceledBy", this.getCanceledBy())
                        .addParameter("beforeEnqueuedAt", this.getBeforeEnqueuedAt())
                        .addParameter("afterEnqueuedAt", this.getAfterEnqueuedAt())
                        .addParameter("beforeStartedAt", this.getBeforeStartedAt())
                        .addParameter("afterStartedAt", this.getAfterStartedAt())
                        .addParameter("beforeFinishedAt", this.getBeforeFinishedAt())
                        .addParameter("afterFinishedAt", this.getAfterFinishedAt());
        return urlb.getURL();
    }
}
