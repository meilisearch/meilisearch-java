package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FacetingTest {
    static Stream<Arguments> jsonHandlers() {
        return Stream.of(
                Arguments.of("Gson", new GsonJsonHandler()),
                Arguments.of("Jackson", new JacksonJsonHandler()),
                Arguments.of("Jackson custom mapper", new JacksonJsonHandler(new ObjectMapper())));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("jsonHandlers")
    void serializesFacetSortValues(String name, JsonHandler handler) {
        Faceting faceting = new Faceting();
        faceting.setMaxValuesPerFacet(200);
        faceting.setSortFacetValuesBy(
                new HashMap<>(
                        Map.of(
                                "*", FacetSortValue.SAMPLED_COUNT,
                                "genres", FacetSortValue.SAMPLED_COUNT,
                                "title", FacetSortValue.ALPHA,
                                "author", FacetSortValue.COUNT)));

        JSONObject json = new JSONObject(handler.encode(faceting));
        JSONObject sortValues = json.getJSONObject("sortFacetValuesBy");

        assertThat(json.getInt("maxValuesPerFacet"), is(200));
        assertThat(sortValues.getString("*"), is("sampledCount"));
        assertThat(sortValues.getString("genres"), is("sampledCount"));
        assertThat(sortValues.getString("title"), is("alpha"));
        assertThat(sortValues.getString("author"), is("count"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("jsonHandlers")
    void deserializesFacetSortValues(String name, JsonHandler handler) {
        String json =
                "{\"maxValuesPerFacet\":200,\"sortFacetValuesBy\":{\"*\":\"sampledCount\","
                        + "\"genres\":\"sampledCount\",\"title\":\"alpha\",\"author\":\"count\"}}";

        Faceting faceting = handler.decode(json, Faceting.class);
        Settings settings = handler.decode("{\"faceting\":" + json + "}", Settings.class);
        Map<String, FacetSortValue> expected =
                Map.of(
                        "*", FacetSortValue.SAMPLED_COUNT,
                        "genres", FacetSortValue.SAMPLED_COUNT,
                        "title", FacetSortValue.ALPHA,
                        "author", FacetSortValue.COUNT);

        assertThat(faceting.getMaxValuesPerFacet(), is(200));
        assertThat(faceting.getSortFacetValuesBy(), is(equalTo(expected)));
        assertThat(settings.getFaceting().getSortFacetValuesBy(), is(equalTo(expected)));
    }
}
