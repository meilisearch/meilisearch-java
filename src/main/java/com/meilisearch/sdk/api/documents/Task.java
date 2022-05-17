package com.meilisearch.sdk.api.documents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private String status;
    private int uid;
    private String indexUid;
    private String type;
    private String duration;
    private String enqueuedAt;
    private String startedAt;
    private String finishedAt;
}
