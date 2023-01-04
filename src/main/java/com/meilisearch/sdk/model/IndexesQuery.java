package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters when fetching indexes
 *
 * <p>https://docs.meilisearch.com/reference/api/indexes.html#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class IndexesQuery {
    private int offset = -1;
    private int limit = -1;

    public IndexesQuery() {}

    public String toQuery(String uid) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid);
        return urlb.getURL();
    }

    public String toQuery(IndexesQuery param) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes")
                .addParameter("limit", param.getLimit())
                .addParameter("offset", param.getOffset());
        return urlb.getURL();
    }
}
