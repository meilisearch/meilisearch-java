package com.meilisearch.sdk.model;

import lombok.Getter;

/** Data structure of Meilisearch response for a Results */
@Getter
public class Results<T> {
    protected T[] results = null;
    protected int limit;
    protected int offset;
    protected int total;

    public Results() {}
}
