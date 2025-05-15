package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class SimilarDocumentRequestTest {

    @Test
    void toStringDefaultValues() {
        SimilarDocumentRequest request = new SimilarDocumentRequest();
        JSONObject json = new JSONObject(request.toString());

        // only id and embedder should be absent (null) by default
        assertThat(json.has("id"), is(false));
        assertThat(json.has("embedder"), is(false));
        assertThat(json.has("attributesToRetrieve"), is(false));
        assertThat(json.has("offset"), is(false));
        assertThat(json.has("limit"), is(false));
        assertThat(json.has("filter"), is(false));
        assertThat(json.has("showRankingScore"), is(false));
        assertThat(json.has("showRankingScoreDetails"), is(false));
        assertThat(json.has("rankingScoreThreshold"), is(false));
        assertThat(json.has("retrieveVectors"), is(false));
    }

    @Test
    void toStringAllParameters() {
        SimilarDocumentRequest request = new SimilarDocumentRequest()
                .setId("123")
                .setEmbedder("custom")
                .setAttributesToRetrieve(new String[]{"title", "description"})
                .setOffset(10)
                .setLimit(20)
                .setFilter("genre = 'action'")
                .setShowRankingScore(true)
                .setShowRankingScoreDetails(true)
                .setRankingScoreThreshold(0.5)
                .setRetrieveVectors(true);

        JSONObject json = new JSONObject(request.toString());
        assertThat(json.getString("id"), is(equalTo("123")));
        assertThat(json.getString("embedder"), is(equalTo("custom")));
        assertThat(json.getJSONArray("attributesToRetrieve").getString(0), is(equalTo("title")));
        assertThat(json.getJSONArray("attributesToRetrieve").getString(1), is(equalTo("description")));
        assertThat(json.getInt("offset"), is(equalTo(10)));
        assertThat(json.getInt("limit"), is(equalTo(20)));
        assertThat(json.getString("filter"), is(equalTo("genre = 'action'")));
        assertThat(json.getBoolean("showRankingScore"), is(equalTo(true)));
        assertThat(json.getBoolean("showRankingScoreDetails"), is(equalTo(true)));
        assertThat(json.getDouble("rankingScoreThreshold"), is(equalTo(0.5)));
        assertThat(json.getBoolean("retrieveVectors"), is(equalTo(true)));
    }

    @Test
    void gettersAndSetters() {
        SimilarDocumentRequest request = SimilarDocumentRequest.builder()
                .id("123")
                .embedder("custom")
                .attributesToRetrieve(new String[]{"title", "description"})
                .offset(10)
                .limit(20)
                .filter("genre = 'action'")
                .showRankingScore(true)
                .showRankingScoreDetails(true)
                .rankingScoreThreshold(0.5)
                .retrieveVectors(true)
                .build();

        assertThat(request.getId(), is(equalTo("123")));
        assertThat(request.getEmbedder(), is(equalTo("custom")));
        assertThat(request.getAttributesToRetrieve(), is(equalTo(new String[]{"title", "description"})));
        assertThat(request.getOffset(), is(equalTo(10)));
        assertThat(request.getLimit(), is(equalTo(20)));
        assertThat(request.getFilter(), is(equalTo("genre = 'action'")));
        assertThat(request.getShowRankingScore(), is(equalTo(true)));
        assertThat(request.getShowRankingScoreDetails(), is(equalTo(true)));
        assertThat(request.getRankingScoreThreshold(), is(equalTo(0.5)));
        assertThat(request.getRetrieveVectors(), is(equalTo(true)));
    }
}
