package com.meilisearch.sdk;

import lombok.Getter;
import lombok.ToString;

/** Meilisearch dump */
@Getter
public class Dump {
    private String status;
    private String uid;
    @ToString.Exclude private String startedAt;
    @ToString.Exclude private String finishedAt;
}
