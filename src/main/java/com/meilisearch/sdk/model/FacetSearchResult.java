package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch facet search response data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/facet_search#response">API
 *     specification</a>
 */
@Getter
@ToString
public class FacetSearchResult implements FacetSearchable {
    ArrayList<HashMap<String, Object>> facetHits;
    int processingTimeMs;
    String facetQuery;

    public FacetSearchResult() {}
}
