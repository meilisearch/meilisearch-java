package com.meilisearch.sdk.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.FilterableAttributesFeatures;
import com.meilisearch.sdk.model.FilterableAttributesFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Gson adapter for polymorphic filterable attributes config (string or object). */
public class GsonFilterableAttributesConfigTypeAdapter
        extends TypeAdapter<FilterableAttributesConfig> {

    @Override
    public void write(JsonWriter writer, FilterableAttributesConfig value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        String[] patterns = value.getAttributePatterns();
        FilterableAttributesFeatures features = value.getFeatures();
        boolean canBeString =
                features == null && patterns != null && patterns.length == 1 && patterns[0] != null;

        if (canBeString) {
            writer.value(patterns[0]);
            return;
        }

        writer.beginObject();
        writeAttributePatterns(writer, patterns);
        writeFeatures(writer, features);
        writer.endObject();
    }

    @Override
    public FilterableAttributesConfig read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        if (reader.peek() == JsonToken.STRING) {
            return FilterableAttributesConfig.fromAttributeName(reader.nextString());
        }

        FilterableAttributesConfig config = new FilterableAttributesConfig();

        reader.beginObject();
        while (reader.peek() != JsonToken.END_OBJECT) {
            switch (reader.nextName()) {
                case "attributePatterns":
                    config.setAttributePatterns(readAttributePatterns(reader));
                    break;
                case "features":
                    config.setFeatures(readFeatures(reader));
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }

    private void writeAttributePatterns(JsonWriter writer, String[] patterns) throws IOException {
        if (patterns == null) {
            return;
        }
        writer.name("attributePatterns").beginArray();
        for (String pattern : patterns) {
            writer.value(pattern);
        }
        writer.endArray();
    }

    private void writeFeatures(JsonWriter writer, FilterableAttributesFeatures features)
            throws IOException {
        if (features == null) {
            return;
        }
        writer.name("features").beginObject();
        if (features.getFacetSearch() != null) {
            writer.name("facetSearch").value(features.getFacetSearch());
        }
        writeFilter(writer, features.getFilter());
        writer.endObject();
    }

    private void writeFilter(JsonWriter writer, FilterableAttributesFilter filter)
            throws IOException {
        if (filter == null) {
            return;
        }
        writer.name("filter").beginObject();
        if (filter.getEquality() != null) {
            writer.name("equality").value(filter.getEquality());
        }
        if (filter.getComparison() != null) {
            writer.name("comparison").value(filter.getComparison());
        }
        writer.endObject();
    }

    private String[] readAttributePatterns(JsonReader reader) throws IOException {
        List<String> patterns = new ArrayList<>();
        reader.beginArray();
        while (reader.peek() != JsonToken.END_ARRAY) {
            patterns.add(reader.nextString());
        }
        reader.endArray();
        return patterns.toArray(new String[0]);
    }

    private FilterableAttributesFeatures readFeatures(JsonReader reader) throws IOException {
        FilterableAttributesFeatures features = new FilterableAttributesFeatures();
        reader.beginObject();
        while (reader.peek() != JsonToken.END_OBJECT) {
            switch (reader.nextName()) {
                case "facetSearch":
                    features.setFacetSearch(readNullableBoolean(reader));
                    break;
                case "filter":
                    features.setFilter(readFilter(reader));
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return features;
    }

    private FilterableAttributesFilter readFilter(JsonReader reader) throws IOException {
        FilterableAttributesFilter filter = new FilterableAttributesFilter();
        reader.beginObject();
        while (reader.peek() != JsonToken.END_OBJECT) {
            switch (reader.nextName()) {
                case "equality":
                    filter.setEquality(readNullableBoolean(reader));
                    break;
                case "comparison":
                    filter.setComparison(readNullableBoolean(reader));
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return filter;
    }

    private Boolean readNullableBoolean(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return reader.nextBoolean();
    }
}
