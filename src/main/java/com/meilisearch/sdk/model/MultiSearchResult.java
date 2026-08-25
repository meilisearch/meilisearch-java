package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Multi search response
 *
 * <p>https://www.meilisearch.com/docs/reference/api/multi_search#response
 */
@Getter
@ToString
public class MultiSearchResult implements Searchable {
    String indexUid;
    ArrayList<HashMap<String, Object>> hits;
    HashMap<String, HashMap<String, Integer>> facetDistribution;
    HashMap<String, FacetRating> facetStats;
    int processingTimeMs;
    String query;
    int offset;
    int limit;
    int estimatedTotalHits;
    HashMap<String, FacetsByIndexInfo> facetsByIndex;
    HashMap<String, Object> performanceDetails;

    public MultiSearchResult() {}
}
