package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters of the documents route when retrieving multiple documents
 *
 * <p>https://www.meilisearch.com/docs/reference/api/documents#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class DocumentsQuery {
    private int offset = -1;
    private int limit = -1;
    private String[] fields;

    public DocumentsQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("offset", this.getOffset())
                        .addParameter("fields", this.getFields());
        return urlb.getURL();
    }
}
