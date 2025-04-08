package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class SimilarDocumentRequestTest {

    @Test
    void toStringSimpleRequest() {
        SimilarDocumentRequest classToTest = new SimilarDocumentRequest().setId("123");

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
    }

    @Test
    void toStringWithEmbedder() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setEmbedder("custom");

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getString("embedder"), is(equalTo("custom")));
    }

    @Test
    void toStringWithAttributesToRetrieve() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest()
                        .setId("123")
                        .setAttributesToRetrieve(new String[] {"title", "description"});

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(
                jsonObject.getJSONArray("attributesToRetrieve").getString(0), is(equalTo("title")));
        assertThat(
                jsonObject.getJSONArray("attributesToRetrieve").getString(1),
                is(equalTo("description")));
    }

    @Test
    void toStringWithOffsetAndLimit() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setOffset(10).setLimit(20);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getInt("offset"), is(equalTo(10)));
        assertThat(jsonObject.getInt("limit"), is(equalTo(20)));
    }

    @Test
    void toStringWithFilter() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setFilter("genre = 'action'");

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getString("filter"), is(equalTo("genre = 'action'")));
    }

    @Test
    void toStringWithShowRankingScore() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setShowRankingScore(true);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getBoolean("showRankingScore"), is(equalTo(true)));
    }

    @Test
    void toStringWithShowRankingScoreDetails() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setShowRankingScoreDetails(true);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getBoolean("showRankingScoreDetails"), is(equalTo(true)));
    }

    @Test
    void toStringWithRankingScoreThreshold() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setRankingScoreThreshold(0.5);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getDouble("rankingScoreThreshold"), is(equalTo(0.5)));
    }

    @Test
    void toStringWithRetrieveVectors() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest().setId("123").setRetrieveVectors(true);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getBoolean("retrieveVectors"), is(equalTo(true)));
    }

    @Test
    void toStringWithAllParameters() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest()
                        .setId("123")
                        .setEmbedder("custom")
                        .setAttributesToRetrieve(new String[] {"title", "description"})
                        .setOffset(10)
                        .setLimit(20)
                        .setFilter("genre = 'action'")
                        .setShowRankingScore(true)
                        .setShowRankingScoreDetails(true)
                        .setRankingScoreThreshold(0.5)
                        .setRetrieveVectors(true);

        JSONObject jsonObject = new JSONObject(classToTest.toString());
        assertThat(jsonObject.getString("id"), is(equalTo("123")));
        assertThat(jsonObject.getString("embedder"), is(equalTo("custom")));
        assertThat(
                jsonObject.getJSONArray("attributesToRetrieve").getString(0), is(equalTo("title")));
        assertThat(
                jsonObject.getJSONArray("attributesToRetrieve").getString(1),
                is(equalTo("description")));
        assertThat(jsonObject.getInt("offset"), is(equalTo(10)));
        assertThat(jsonObject.getInt("limit"), is(equalTo(20)));
        assertThat(jsonObject.getString("filter"), is(equalTo("genre = 'action'")));
        assertThat(jsonObject.getBoolean("showRankingScore"), is(equalTo(true)));
        assertThat(jsonObject.getBoolean("showRankingScoreDetails"), is(equalTo(true)));
        assertThat(jsonObject.getDouble("rankingScoreThreshold"), is(equalTo(0.5)));
        assertThat(jsonObject.getBoolean("retrieveVectors"), is(equalTo(true)));
    }

    @Test
    void gettersAndSetters() {
        SimilarDocumentRequest classToTest =
                new SimilarDocumentRequest()
                        .setId("123")
                        .setEmbedder("custom")
                        .setAttributesToRetrieve(new String[] {"title", "description"})
                        .setOffset(10)
                        .setLimit(20)
                        .setFilter("genre = 'action'")
                        .setShowRankingScore(true)
                        .setShowRankingScoreDetails(true)
                        .setRankingScoreThreshold(0.5)
                        .setRetrieveVectors(true);

        assertThat(classToTest.getId(), is(equalTo("123")));
        assertThat(classToTest.getEmbedder(), is(equalTo("custom")));
        assertThat(
                classToTest.getAttributesToRetrieve(),
                is(equalTo(new String[] {"title", "description"})));
        assertThat(classToTest.getOffset(), is(equalTo(10)));
        assertThat(classToTest.getLimit(), is(equalTo(20)));
        assertThat(classToTest.getFilter(), is(equalTo("genre = 'action'")));
        assertThat(classToTest.getShowRankingScore(), is(equalTo(true)));
        assertThat(classToTest.getShowRankingScoreDetails(), is(equalTo(true)));
        assertThat(classToTest.getRankingScoreThreshold(), is(equalTo(0.5)));
        assertThat(classToTest.getRetrieveVectors(), is(equalTo(true)));
    }

    @Test
    void defaultValues() {
        SimilarDocumentRequest classToTest = new SimilarDocumentRequest();

        assertThat(classToTest.getId(), is(nullValue()));
        assertThat(classToTest.getEmbedder(), is(nullValue()));
        assertThat(classToTest.getAttributesToRetrieve(), is(nullValue()));
        assertThat(classToTest.getOffset(), is(nullValue()));
        assertThat(classToTest.getLimit(), is(nullValue()));
        assertThat(classToTest.getFilter(), is(nullValue()));
        assertThat(classToTest.getShowRankingScore(), is(nullValue()));
        assertThat(classToTest.getShowRankingScoreDetails(), is(nullValue()));
        assertThat(classToTest.getRankingScoreThreshold(), is(nullValue()));
        assertThat(classToTest.getRetrieveVectors(), is(nullValue()));
    }
}
