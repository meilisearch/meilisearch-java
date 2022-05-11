package com.meilisearch.sdk.api.index;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Index {
    private String uid;
    private String primaryKey;
    private String createdAt;
    private String updatedAt;
}
