package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of the query parameters for the keys routes
 *
 * <p>https://docs.meilisearch.com/reference/api/keys.html#query-parameters
 */
@Setter
@Getter
@Accessors(chain = true)
public class KeysQuery {
    private int offset = -1;
    private int limit = -1;

    public KeysQuery() {}

    public String toQuery(String key) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("keys").addSubroute(key);
        return urlb.getURL();
    }

    public String toQuery(KeysQuery param) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("keys")
                .addParameter("limit", param.getLimit())
                .addParameter("offset", param.getOffset());
        return urlb.getURL();
    }
}
