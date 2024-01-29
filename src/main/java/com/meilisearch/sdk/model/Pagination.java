package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pagination {
    protected int maxTotalHits;

    public Pagination() {}

    public Pagination(int maxTotalHits) {
        this.maxTotalHits = maxTotalHits;
    }
}
