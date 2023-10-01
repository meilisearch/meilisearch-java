package com.meilisearch.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search response data structure for limited pagination
 *
 * <p><a href="https://www.meilisearch.com/docs/learn/front_end/pagination#numbered-page-selectors">https://www.meilisearch.com/docs/learn/front_end/pagination#numbered-page-selectors</a>
 */
@Getter
@ToString
public class SearchResultPaginated implements Searchable {
    protected int totalHits;
    protected int hitsPerPage;
    protected int page;
    protected int totalPages;
    ArrayList<HashMap<String, Object>> hits;
    Object facetDistribution;
    int processingTimeMs;
    String query;

    public SearchResultPaginated() {}
}
