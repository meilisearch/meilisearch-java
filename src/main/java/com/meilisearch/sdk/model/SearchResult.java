package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search response data structure
 *
 * <p>https://docs.meilisearch.com/references/search.html
 */
@Getter
@ToString
public class SearchResult implements Serializable {
    protected ArrayList<HashMap<String, Object>> hits;
    protected int offset;
    protected int limit;
    protected int estimatedTotalHits;
    protected Object facetDistribution;
    protected int processingTimeMs;
    protected String query;
    protected int totalHits;
    protected int hitsPerPage;
    protected int page;
    protected int totalPages;

    public SearchResult() {}
}
