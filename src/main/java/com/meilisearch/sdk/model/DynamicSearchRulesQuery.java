package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.Map;

public class DynamicSearchRulesQuery implements Serializable {
    protected Integer offset;
    protected Integer limit;
    protected Map<String, Object> filter;

    public DynamicSearchRulesQuery() {}

    public DynamicSearchRulesQuery setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public DynamicSearchRulesQuery setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public DynamicSearchRulesQuery setFilter(Map<String, Object> filter) {
        this.filter = filter;
        return this;
    }
}
