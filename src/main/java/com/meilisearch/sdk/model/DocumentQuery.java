package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data structure of the query parameters of the documents route when retrieving a document
 *
 * <p>https://docs.meilisearch.com/reference/api/documents.html#query-parameters
 */
@Setter
@Getter
public class DocumentQuery {
    private String[] fields;

    public DocumentQuery() {}

    public String toQuery() {
        URLBuilder urlb = new URLBuilder().addParameter("fields", this.getFields());
        return urlb.getURL();
    }
}
