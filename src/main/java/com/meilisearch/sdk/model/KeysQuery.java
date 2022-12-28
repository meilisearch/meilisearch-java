package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of a query parameter for keys route
 *
 * <p>https://docs.meilisearch.com/reference/api/keys.html#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class KeysQuery {
    private int offset = -1;
    private int limit = -1;

    public KeysQuery() {}
}
