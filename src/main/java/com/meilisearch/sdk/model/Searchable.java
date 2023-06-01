package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Meilisearch search common response data structure
 *
 * <p>https://www.meilisearch.com/docs/references/search
 */
public interface Searchable {
    ArrayList<HashMap<String, Object>> getHits();

    Object getFacetDistribution();

    int getProcessingTimeMs();

    String getQuery();
}
