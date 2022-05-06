package com.meilisearch.sdk.api.documents;

import lombok.Getter;
import lombok.Setter;

public class Task {
    @Getter @Setter  private String status;
    @Getter @Setter  private int uid;
    @Getter @Setter  private String indexUid;
    @Getter @Setter  private String type;
    @Getter @Setter  private String duration;
    @Getter @Setter  private String enqueuedAt;
    @Getter @Setter  private String startedAt;
    @Getter @Setter  private String finishedAt;
}
