package com.meilisearch.sdk.model;

import com.meilisearch.sdk.http.URLBuilder;
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

    public String toQuery(String uid) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid).addSubroute("documents");
        return urlb.getURL();
    }

    public String toQuery(String uid, String identifier) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes")
                .addSubroute(uid)
                .addSubroute("documents")
                .addSubroute(identifier);
        return urlb.getURL();
    }

    public String toQuery(String uid, DocumentsQuery param) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes")
                .addSubroute(uid)
                .addSubroute("documents")
                .addParameter("limit", param.getLimit())
                .addParameter("offset", param.getOffset())
                .addParameter("fields", param.getFields());
        return urlb.getURL();
    }

    public String toQuery(String uid, String identifier, DocumentsQuery param) {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes")
                .addSubroute(uid)
                .addSubroute("documents")
                .addSubroute(identifier)
                .addParameter("limit", param.getLimit())
                .addParameter("offset", param.getOffset())
                .addParameter("fields", param.getFields());
        return urlb.getURL();
    }
}
