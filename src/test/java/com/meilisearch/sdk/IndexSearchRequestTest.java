package com.meilisearch.sdk;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.model.Hybrid;
import org.junit.jupiter.api.Test;

class IndexSearchRequestTest {
    @Test
    void toStringWithHybridUsingBuilder() {
        IndexSearchRequest classToTest =
                IndexSearchRequest.builder()
                        .q("This is a Test")
                        .hybrid(Hybrid.builder().semanticRatio(0.5).embedder("default").build())
                        .build();

        String expected =
                "{\"q\":\"This is a Test\",\"hybrid\":{\"semanticRatio\":0.5,\"embedder\":\"default\"}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));

        // Verify getters
        assertThat(classToTest.getHybrid().getSemanticRatio(), is(equalTo(0.5)));
        assertThat(classToTest.getHybrid().getEmbedder(), is(equalTo("default")));
    }

    @Test
    void toStringWithHybridAndOtherParameters() {
        IndexSearchRequest classToTest =
                IndexSearchRequest.builder()
                        .q("This is a Test")
                        .offset(200)
                        .limit(900)
                        .hybrid(
                                Hybrid.builder()
                                        .semanticRatio(0.7)
                                        .embedder("custom-embedder")
                                        .build())
                        .build();

        String expected =
                "{\"q\":\"This is a Test\",\"hybrid\":{\"semanticRatio\":0.7,\"embedder\":\"custom-embedder\"},\"offset\":200,\"limit\":900}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }

    @Test
    void toStringWithHybridOnlyEmbedder() {
        IndexSearchRequest classToTest =
                new IndexSearchRequest("someIndexUuid")
                        .setQuery("This is a Test")
                        .setHybrid(Hybrid.builder().embedder("default").build());

        String expected = "{\"q\":\"This is a Test\",\"hybrid\":{\"embedder\":\"default\"}}";
        assertThat(classToTest.toString(), is(equalTo(expected)));
    }



    @Test
    void toStringWithInlineRule() {
        Map<String, Object> scaleRule = new HashMap<>();
        scaleRule.put("filter", "hero = batman");
        scaleRule.put("weight", 1.2);

        Map<String, Object> actions = new HashMap<>();
        actions.put("scale", java.util.Collections.singletonList(scaleRule));

        Map<String, Object> inlineRule = new HashMap<>();
        inlineRule.put("uid", "user-likes-batman");
        inlineRule.put("actions", actions);

        IndexSearchRequest classToTest =
            IndexSearchRequest.builder()
                .q("batman")
                .inlineRule(inlineRule)
                .build();

        org.json.JSONObject actual = new org.json.JSONObject(classToTest.toString());

        assertThat(actual.getString("q"), is(equalTo("batman")));
        assertThat(
            actual.getJSONObject("inlineRule").getString("uid"),
            is(equalTo("user-likes-batman")));
    }


}
