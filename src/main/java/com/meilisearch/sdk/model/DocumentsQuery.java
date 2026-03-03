package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

/**
 * Data structure of the query parameters or request body params of the documents route when
 * retrieving multiple documents
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#query-parameters">API
 *     specification</a>
 */
@Setter
@Getter
@Accessors(chain = true)
public class DocumentsQuery {
    private int offset = -1;
    private int limit = -1;
    private String[] fields;
    private String[] filter;
    private String[] sort;

    public DocumentsQuery() {}

    public String toQuery() {
        URLBuilder urlb =
                new URLBuilder()
                        .addParameter("limit", this.getLimit())
                        .addParameter("offset", this.getOffset())
                        .addParameter("fields", this.getFields())
                        .addParameter("sort", this.getSort());
        return urlb.getURL();
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        if (offset > -1) {
            jsonObject.put("offset", offset);
        }
        if (limit > -1) {
            jsonObject.put("limit", limit);
        }
        if (fields != null) {
            jsonObject.put("fields", fields);
        }
        if (filter != null) {
            jsonObject.put("filter", filter);
        }
        if (sort != null) {
            jsonObject.put("sort", sort);
        }
        return jsonObject.toString();
    }
}
