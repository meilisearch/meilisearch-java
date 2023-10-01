package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch search common response data structure
 *
 * <p><a href="https://www.meilisearch.com/docs/references/search">https://www.meilisearch.com/docs/references/search</a>
 */
public interface Searchable {
    ArrayList<HashMap<String, Object>> getHits();

    Object getFacetDistribution();

    int getProcessingTimeMs();

    String getQuery();
}
