package com.meilisearch.sdk.api.index;

import lombok.Getter;
import lombok.Setter;

public class Index {
    @Getter @Setter  private String uid;
    @Getter @Setter  private String primaryKey;
    @Getter @Setter  private String createdAt;
    @Getter @Setter  private String updatedAt;
}
