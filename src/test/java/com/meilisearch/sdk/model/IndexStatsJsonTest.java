package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class IndexStatsJsonTest {

    private static final String INDEX_STATS_JSON = "{\"numberOfDocuments\":3,\"isIndexing\":true}";

    static Stream<JsonHandler> handlers() {
        return Stream.of(new GsonJsonHandler(), new JacksonJsonHandler());
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void decodesIsIndexing(JsonHandler handler) throws Exception {
        IndexStats stats = handler.decode(INDEX_STATS_JSON, IndexStats.class);

        assertThat(stats.isIndexing(), is(true));
        assertThat(stats.getNumberOfDocuments(), is(equalTo(3L)));
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void decodesIsIndexingInsideGlobalStats(JsonHandler handler) throws Exception {
        Stats stats =
                handler.decode("{\"indexes\":{\"movies\":" + INDEX_STATS_JSON + "}}", Stats.class);

        assertThat(stats.getIndexes().get("movies").isIndexing(), is(true));
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void encodesIsIndexingWithApiFieldName(JsonHandler handler) throws Exception {
        IndexStats stats = new IndexStats(3, true, null, 0, 0, 0, 0);

        JSONObject json = new JSONObject(handler.encode(stats));

        assertThat(json.getBoolean("isIndexing"), is(true));
        assertThat(json.has("indexing"), is(false));
    }
}
