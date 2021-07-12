package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/** Result of `search` API Refer https://docs.meilisearch.com/references/search.html */
@ToString
public class SearchResult implements Serializable {

    @Getter ArrayList<HashMap<String, Object>> hits;

    @Getter int offset;

    @Getter int limit;

    @Getter int nbHits;

    @Getter boolean exhaustiveNbHits;

    @Getter Object facetsDistribution;

    @Getter boolean exhaustiveFacetsCount;

    @Getter int processingTimeMs;

    @Getter String query;
}
