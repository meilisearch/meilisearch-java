package com.meilisearch.sdk;

import lombok.Getter;
import lombok.ToString;

/** MeiliSearch dump */
public class Dump {
    @Getter private String status;
    @Getter private String uid;
    @Getter @ToString.Exclude private String startedAt;
    @Getter @ToString.Exclude private String finishedAt;
}
