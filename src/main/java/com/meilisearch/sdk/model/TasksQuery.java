package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of a query parameter for tasks route
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#query-parameters">API
 *     specification</a>
 */
@Setter
@Getter
@Accessors(chain = true)
public class TasksQuery {
    private int[] uids;
    private int limit = -1;
    private int from = -1;
    private String[] statuses;
    private String[] types;
    private String[] indexUids;
    private int[] canceledBy;
    private boolean reverse;
    private Date beforeEnqueuedAt;
    private Date afterEnqueuedAt;
    private Date beforeStartedAt;
    private Date afterStartedAt;
    private Date beforeFinishedAt;
    private Date afterFinishedAt;

    public TasksQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("from", this.getFrom())
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
                        .addParameter("afterFinishedAt", this.getAfterFinishedAt())
                        .addParameter("reverse", String.valueOf(this.isReverse()));
        return urlb.getURL();
    }
}
