package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Data Structure Result of `search`
 *
 * <p>https://docs.meilisearch.com/references/search.html
 */
@Getter
@ToString
public class SearchResult implements Serializable {
    protected ArrayList<HashMap<String, Object>> hits;
    protected int offset;
    protected int limit;
    protected int nbHits;
    protected boolean exhaustiveNbHits;
    protected Object facetsDistribution;
    protected boolean exhaustiveFacetsCount;
    protected int processingTimeMs;
    protected String query;
}
