package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters when fetching indexes
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes#query-parameters">API
 *     specification</a>
 */
@Setter
@Getter
@Accessors(chain = true)
public class IndexesQuery {
    private int offset = -1;
    private int limit = -1;

    public IndexesQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("offset", this.getOffset());
        return urlb.getURL();
    }
}
