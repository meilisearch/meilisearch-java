package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BatchesQuery {
    private int[] uids;
    private int[] batchUids;
    private String[] types;
    private String[] statuses;
    private String[] indexUids;
    private int[] canceledBy;
    private Date beforeEnqueuedAt;
    private Date afterEnqueuedAt;
    private Date beforeStartedAt;
    private Date afterStartedAt;
    private Date beforeFinishedAt;
    private Date afterFinishedAt;
    private int limit = -1;
    private int from = -1;

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("uids", this.getUids())
                        .addParameter("batchUids", this.getBatchUids())
                        .addParameter("types", this.getTypes())
                        .addParameter("statuses", this.getStatuses())
                        .addParameter("indexUids", this.getIndexUids())
                        .addParameter("canceledBy", this.getCanceledBy())
                        .addParameter("beforeEnqueuedAt", this.getBeforeEnqueuedAt())
                        .addParameter("afterEnqueuedAt", this.getAfterEnqueuedAt())
                        .addParameter("beforeStartedAt", this.getBeforeStartedAt())
                        .addParameter("afterStartedAt", this.getAfterStartedAt())
                        .addParameter("beforeFinishedAt", this.getBeforeFinishedAt())
                        .addParameter("afterFinishedAt", this.getAfterFinishedAt())
                        .addParameter("limit", this.getLimit())
                        .addParameter("from", this.getFrom());
        return urlb.getURL();
    }
}
