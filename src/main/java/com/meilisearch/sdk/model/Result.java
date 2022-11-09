package com.meilisearch.sdk.model;

import lombok.Getter;

/** Data structure of Meilisearch response for a Result */
@Getter
public class Result<T> {
    protected T[] results = null;

    public Result() {}
}
