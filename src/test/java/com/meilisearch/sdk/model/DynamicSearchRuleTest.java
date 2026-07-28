package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.json.GsonJsonHandler;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class DynamicSearchRuleTest {
    private final GsonJsonHandler jsonHandler = new GsonJsonHandler();

    @Test
    void serializesFilterConditionValues() {
        Map<String, Object> conditions =
                Map.of(
                        "filter",
                        Map.of(
                                "values",
                                Map.of(
                                        "color", "red",
                                        "category", "shirt")));
        Map<String, Object> action =
                Map.of(
                        "selector", Map.of("indexUid", "products", "id", "123"),
                        "action", Map.of("type", "pin", "position", 1));

        DynamicSearchRule rule =
                new DynamicSearchRule(
                        "test-filter", "Filter rule", 1, true, conditions, List.of(action));

        JSONObject json = new JSONObject(jsonHandler.encode(rule));
        JSONObject values =
                json.getJSONObject("conditions").getJSONObject("filter").getJSONObject("values");

        assertThat(values.getString("color"), is(equalTo("red")));
        assertThat(values.getString("category"), is(equalTo("shirt")));
    }

    @Test
    void deserializesLastUpdatedAt() {
        String lastUpdatedAt = "2026-07-28T08:30:00Z";
        String response =
                "{\"uid\":\"test-filter\",\"description\":\"Filter rule\","
                        + "\"priority\":1,\"active\":true,\"conditions\":{},\"actions\":[],"
                        + "\"lastUpdatedAt\":\""
                        + lastUpdatedAt
                        + "\"}";

        DynamicSearchRule rule = jsonHandler.decode(response, DynamicSearchRule.class);

        assertThat(rule.getLastUpdatedAt(), is(equalTo(lastUpdatedAt)));
    }
}
