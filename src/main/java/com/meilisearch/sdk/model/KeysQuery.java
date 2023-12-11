package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters for the keys routes
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/keys#query-parameters">API
 *     specification</a>
 */
@Setter
@Getter
@Accessors(chain = true)
public class KeysQuery {
    private int offset = -1;
    private int limit = -1;

    public KeysQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("offset", this.getOffset());
        return urlb.getURL();
    }
}
