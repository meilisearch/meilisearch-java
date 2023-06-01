package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search response data structure for infinite pagination
 *
 * <p>https://www.meilisearch.com/docs/reference/api/search#response
 */
@Getter
@ToString
public class SearchResult implements Searchable {
    ArrayList<HashMap<String, Object>> hits;
    Object facetDistribution;
    int processingTimeMs;
    String query;
    int offset;
    int limit;
    int estimatedTotalHits;

    public SearchResult() {}
}
