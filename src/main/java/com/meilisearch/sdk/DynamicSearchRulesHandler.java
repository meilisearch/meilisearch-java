package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.DynamicSearchRule;
import com.meilisearch.sdk.model.DynamicSearchRulesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;

class DynamicSearchRulesHandler {
    private final HttpClient httpClient;

    protected DynamicSearchRulesHandler(Config config) {
        httpClient = config.httpClient;
    }

    Results<DynamicSearchRule> listDynamicSearchRules(DynamicSearchRulesQuery query)
            throws MeilisearchException {
        return httpClient.post(
                dynamicSearchRulesPath().getURL(), query, Results.class, DynamicSearchRule.class);
    }

    DynamicSearchRule getDynamicSearchRule(String uid) throws MeilisearchException {
        return httpClient.get(
                dynamicSearchRulesPath().addSubroute(uid).getURL(), DynamicSearchRule.class);
    }

    TaskInfo updateDynamicSearchRule(String uid, DynamicSearchRule dynamicSearchRule)
            throws MeilisearchException {
        return httpClient.patch(
                dynamicSearchRulesPath().addSubroute(uid).getURL(),
                dynamicSearchRule,
                TaskInfo.class);
    }

    TaskInfo deleteDynamicSearchRule(String uid) throws MeilisearchException {
        return httpClient.delete(
                dynamicSearchRulesPath().addSubroute(uid).getURL(), TaskInfo.class);
    }

    private URLBuilder dynamicSearchRulesPath() {
        return new URLBuilder("/dynamic-search-rules");
    }
}
