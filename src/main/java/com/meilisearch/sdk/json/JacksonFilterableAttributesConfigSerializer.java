package com.meilisearch.sdk.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.FilterableAttributesFeatures;
import com.meilisearch.sdk.model.FilterableAttributesFilter;
import java.io.IOException;

/** Jackson serializer for polymorphic filterable attributes config (string or object). */
public class JacksonFilterableAttributesConfigSerializer
        extends JsonSerializer<FilterableAttributesConfig> {

    @Override
    public void serialize(
            FilterableAttributesConfig value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String[] patterns = value.getAttributePatterns();
        FilterableAttributesFeatures features = value.getFeatures();
        boolean canBeString =
                features == null && patterns != null && patterns.length == 1 && patterns[0] != null;

        if (canBeString) {
            gen.writeString(patterns[0]);
            return;
        }

        gen.writeStartObject();
        writeAttributePatterns(patterns, gen);
        writeFeatures(features, gen);
        gen.writeEndObject();
    }

    private void writeAttributePatterns(String[] patterns, JsonGenerator gen) throws IOException {
        if (patterns == null) {
            return;
        }
        gen.writeArrayFieldStart("attributePatterns");
        for (String pattern : patterns) {
            gen.writeString(pattern);
        }
        gen.writeEndArray();
    }

    private void writeFeatures(FilterableAttributesFeatures features, JsonGenerator gen)
            throws IOException {
        if (features == null) {
            return;
        }
        gen.writeObjectFieldStart("features");
        if (features.getFacetSearch() != null) {
            gen.writeBooleanField("facetSearch", features.getFacetSearch());
        }
        writeFilter(features.getFilter(), gen);
        gen.writeEndObject();
    }

    private void writeFilter(FilterableAttributesFilter filter, JsonGenerator gen)
            throws IOException {
        if (filter == null) {
            return;
        }
        gen.writeObjectFieldStart("filter");
        if (filter.getEquality() != null) {
            gen.writeBooleanField("equality", filter.getEquality());
        }
        if (filter.getComparison() != null) {
            gen.writeBooleanField("comparison", filter.getComparison());
        }
        gen.writeEndObject();
    }
}
