package com.meilisearch.sdk.api.documents;

import java.util.List;
import lombok.Getter;

public class SearchResponse<T> {
    @Getter private List<T> hits;
    @Getter private int offset;
    @Getter private int limit;
    @Getter private int nbHits;
    @Getter private boolean exhaustiveNbHits;
    @Getter private int processingTimeMs;
    @Getter private String query;
}
