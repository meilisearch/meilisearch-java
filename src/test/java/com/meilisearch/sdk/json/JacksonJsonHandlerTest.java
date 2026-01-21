package com.meilisearch.sdk.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.FilterableAttributesFeatures;
import com.meilisearch.sdk.model.FilterableAttributesFilter;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.utils.Movie;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class JacksonJsonHandlerTest {

    private final ObjectMapper mapper = spy(new ObjectMapper());
    private final JacksonJsonHandler classToTest = new JacksonJsonHandler(mapper);

    @Test
    void serialize() throws Exception {
        assertThat(classToTest.encode("test"), is(equalTo("test")));
        when(mapper.writeValueAsString(any(Movie.class)))
                .thenThrow(new RuntimeException("Oh boy!"));
        assertThrows(RuntimeException.class, () -> classToTest.encode(new Movie()));
    }

    @Test
    void deserialize() {
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class));
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, (Class<?>[]) null));
        assertDoesNotThrow(() -> classToTest.decode("{}", Movie.class, new Class[0]));
    }

    @Test
    void deserializeString() throws Exception {
        String content = "{}";
        assertThat(classToTest.decode(content, String.class), is(equalTo(content)));
    }

    @Test
    void deserializeBodyNull() {
        assertThrows(Exception.class, () -> classToTest.decode(null, List.class, String.class));
    }

    @Test
    @SuppressWarnings({"RedundantArrayCreation", "ConfusingArgumentToVarargsMethod"})
    void deserializeWithParametersEmpty() throws Exception {
        assertThat(classToTest.decode("{}", Movie.class, (Class<?>[]) null), is(notNullValue()));
        assertThat(classToTest.decode("{}", Movie.class, new Class[0]), is(notNullValue()));
    }

    @Test
    void deserializeMap() throws Exception {
        String mapString =
                "{\"commitSha\":\"b46889b5f0f2f8b91438a08a358ba8f05fc09fc1\",\"commitDate\":\"2019-11-15T09:51:54.278247+00:00\",\"pkgVersion\":\"0.1.1\"}";
        HashMap<String, String> decode =
                classToTest.decode(mapString, HashMap.class, String.class, String.class);

        assertThat(decode, notNullValue());
        assertThat(decode, aMapWithSize(3));
    }

    @Test
    void granularFilterableAttributesRoundTripWithCustomMapper() throws Exception {
        ObjectMapper customMapper = new ObjectMapper();
        JacksonJsonHandler handlerWithCustomMapper = new JacksonJsonHandler(customMapper);

        FilterableAttributesFilter filter = new FilterableAttributesFilter();
        filter.setEquality(true);
        filter.setComparison(false);

        FilterableAttributesFeatures features = new FilterableAttributesFeatures();
        features.setFacetSearch(true);
        features.setFilter(filter);

        FilterableAttributesConfig advanced = new FilterableAttributesConfig();
        advanced.setAttributePatterns(new String[] {"director"});
        advanced.setFeatures(features);

        FilterableAttributesConfig[] configs =
                new FilterableAttributesConfig[] {
                    FilterableAttributesConfig.fromAttributeName("genres"), advanced
                };

        String json = handlerWithCustomMapper.encode(configs);
        FilterableAttributesConfig[] decoded =
                handlerWithCustomMapper.decode(json, FilterableAttributesConfig[].class);

        assertThat(decoded, is(notNullValue()));
        assertThat(decoded.length, is(2));

        FilterableAttributesConfig decodedAdvanced = decoded[1];
        assertThat(decodedAdvanced.getAttributePatterns()[0], is("director"));
        assertThat(decodedAdvanced.getFeatures(), is(notNullValue()));
        assertThat(decodedAdvanced.getFeatures().getFacetSearch(), is(true));
        assertThat(decodedAdvanced.getFeatures().getFilter(), is(notNullValue()));
        assertThat(decodedAdvanced.getFeatures().getFilter().getEquality(), is(true));
        assertThat(decodedAdvanced.getFeatures().getFilter().getComparison(), is(false));
    }

    @Test
    void settingsWithGranularFilterableAttributesRoundTripWithCustomMapper() throws Exception {
        ObjectMapper customMapper = new ObjectMapper();
        JacksonJsonHandler handlerWithCustomMapper = new JacksonJsonHandler(customMapper);

        FilterableAttributesConfig[] configs =
                new FilterableAttributesConfig[] {
                    FilterableAttributesConfig.fromAttributeName("genres"),
                    FilterableAttributesConfig.fromAttributeName("director")
                };

        Settings settings = new Settings();
        settings.setFilterableAttributes(configs);

        String json = handlerWithCustomMapper.encode(settings);
        Settings decoded = handlerWithCustomMapper.decode(json, Settings.class);

        assertThat(decoded, is(notNullValue()));
        assertThat(decoded.getFilterableAttributes(), is(notNullValue()));
        assertThat(decoded.getFilterableAttributes().length, is(2));
        assertThat(
                decoded.getFilterableAttributes()[0].getAttributePatterns()[0], is("genres"));
        assertThat(
                decoded.getFilterableAttributes()[1].getAttributePatterns()[0], is("director"));
    }
}
