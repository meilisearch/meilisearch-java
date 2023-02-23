package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Meilisearch search response data structure for limited pagination
 *
 * <p>https://docs.meilisearch.com/learn/advanced/pagination.html#numbered-page-selectors
 */
@Getter
@ToString
public class SearchResultPaginated extends Searcheable {
    protected int totalHits;
    protected int hitsPerPage;
    protected int page;
    protected int totalPages;

    public SearchResultPaginated() {}
}
