package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search response data structure for infinite pagination
 *
 * <p>https://docs.meilisearch.com/references/search.html
 */
@Getter
@ToString
public class SearchResult extends Searcheable {
    protected int offset;
    protected int limit;
    protected int estimatedTotalHits;

    public SearchResult() {}
}
