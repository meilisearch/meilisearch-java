package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.SearchResult;
import org.junit.jupiter.api.Test;

class SearchResultQueryVectorTest {

    @Test
    void testQueryVectorIsMapped() throws Exception {
        String json =
                "{"
                        + "\"hits\": [],"
                        + "\"processingTimeMs\": 1,"
                        + "\"query\": \"hello\","
                        + "\"queryVector\": [0.1, 0.2, 0.3]"
                        + "}";

        GsonJsonHandler handler = new GsonJsonHandler();
        SearchResult result = handler.decode(json, SearchResult.class);

        assertThat(result, is(notNullValue()));
        assertThat(result.getQueryVector(), is(notNullValue()));
        assertThat(result.getQueryVector().size(), is(3));
        assertThat(result.getQueryVector().get(0), equalTo(0.1F));
        assertThat(result.getQueryVector().get(1), equalTo(0.2F));
        assertThat(result.getQueryVector().get(2), equalTo(0.3F));
    }
}
