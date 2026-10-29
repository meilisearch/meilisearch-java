package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class FacetSearchRequestTest {
    @Test
    void omitsExhaustiveFacetCountByDefault() {
        JSONObject json = new JSONObject(new FacetSearchRequest("genres").toString());

        assertThat(json.has("exhaustiveFacetCount"), is(false));
    }

    @Test
    void serializesExhaustiveFacetCount() {
        FacetSearchRequest request = new FacetSearchRequest("genres").setExhaustiveFacetCount(true);

        JSONObject json = new JSONObject(request.toString());

        assertThat(json.getBoolean("exhaustiveFacetCount"), is(true));
    }

    @Test
    void serializesExhaustiveFacetCountWhenFalse() {
        FacetSearchRequest request =
                new FacetSearchRequest("genres").setExhaustiveFacetCount(false);

        JSONObject json = new JSONObject(request.toString());

        assertThat(json.has("exhaustiveFacetCount"), is(true));
        assertThat(json.getBoolean("exhaustiveFacetCount"), is(false));
    }

    @Test
    void buildsWithExhaustiveFacetCount() {
        FacetSearchRequest request =
                FacetSearchRequest.builder().facetName("genres").exhaustiveFacetCount(true).build();

        assertThat(request.getExhaustiveFacetCount(), is(equalTo(true)));
    }
}
