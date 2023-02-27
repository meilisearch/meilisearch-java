package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch search common response data structure
 *
 * <p>https://docs.meilisearch.com/references/search.html
 */
public interface Searchable {
    ArrayList<HashMap<String, Object>> getHits();

    Object getFacetDistribution();

    int getProcessingTimeMs();

    String getQuery();
}
