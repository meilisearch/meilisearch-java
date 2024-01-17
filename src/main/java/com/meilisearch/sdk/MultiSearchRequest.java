package com.meilisearch.sdk;

import java.util.ArrayList;
import lombok.*;

public class MultiSearchRequest {
    private ArrayList<IndexSearchRequest> queries;

    public MultiSearchRequest() {
        this.queries = new ArrayList();
    }

    /**
     * Method to add new Query
     *
     * @param search Query IndexSearchRequest
     * @return MultiSearchRequest
     */
    public MultiSearchRequest addQuery(IndexSearchRequest search) {
        this.queries.add(search);
        return this;
    }
}
