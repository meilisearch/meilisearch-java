package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.GsonJsonHandler;
import org.junit.jupiter.api.Test;

class MultiSearchFederationTest {

    private final GsonJsonHandler jsonHandler = new GsonJsonHandler();

    @Test
    void serializesDistinctInFederationOptions() throws MeilisearchException {
        MultiSearchFederation federation = new MultiSearchFederation().setDistinct("movie_id");

        assertThat(jsonHandler.encode(federation), containsString("\"distinct\":\"movie_id\""));
    }

    @Test
    void omitsDistinctWhenNotSet() throws MeilisearchException {
        MultiSearchFederation federation = new MultiSearchFederation().setLimit(10);

        assertThat(jsonHandler.encode(federation), not(containsString("distinct")));
    }
}
