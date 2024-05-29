package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch search common response data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/search">API specification</a>
 */
public interface Searchable {
    ArrayList<HashMap<String, Object>> getHits();

    Object getFacetDistribution();

    HashMap<String, FacetRating> getFacetStats();

    int getProcessingTimeMs();

    String getQuery();
}
