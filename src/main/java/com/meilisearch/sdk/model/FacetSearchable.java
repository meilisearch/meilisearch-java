package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch facet search response data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/facet_search">API specification</a>
 */
public interface FacetSearchable {
    ArrayList<HashMap<String, Object>> getFacetHits();

    int getProcessingTimeMs();

    String getFacetQuery();
}
