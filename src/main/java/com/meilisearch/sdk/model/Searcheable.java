package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search common response data structure
 *
 * <p>https://docs.meilisearch.com/references/search.html
 */
@Getter
@ToString
public class Searcheable implements Serializable {
    protected ArrayList<HashMap<String, Object>> hits;
    protected Object facetDistribution;
    protected int processingTimeMs;
    protected String query;

    public Searcheable() {}
}
