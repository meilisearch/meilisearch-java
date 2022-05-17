package com.meilisearch.sdk.api.documents;

import java.util.List;
import lombok.Getter;

@Getter
public class SearchResponse<T> {
    private List<T> hits;
    private int offset;
    private int limit;
    private int nbHits;
    private boolean exhaustiveNbHits;
    private int processingTimeMs;
    private String query;
}
