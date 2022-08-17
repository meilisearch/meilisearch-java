package com.meilisearch.sdk.model;

import lombok.Getter;

/** Data structure of MeiliSearch response for a Result */
@Getter
public class Result<T> {
    protected T[] results = null;

    public Result() {}
}
