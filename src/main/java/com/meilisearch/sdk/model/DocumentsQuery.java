package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of a query parameter for documents route
 *
 * <p>https://docs.meilisearch.com/reference/api/documents.html#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class DocumentsQuery {
    private int offset = -1;
    private int limit = -1;
    private String[] fields;

    public DocumentsQuery() {}
}
