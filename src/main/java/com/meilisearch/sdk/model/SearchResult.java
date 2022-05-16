package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/** Result of `search` API Refer https://docs.meilisearch.com/references/search.html */
@Getter
@ToString
public class SearchResult implements Serializable {

    ArrayList<HashMap<String, Object>> hits;

    int offset;

    int limit;

    int nbHits;

    boolean exhaustiveNbHits;

    Object facetsDistribution;

    boolean exhaustiveFacetsCount;

    int processingTimeMs;

    String query;
}
