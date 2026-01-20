package com.meilisearch.sdk.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.meilisearch.sdk.model.FilterableAttributesRule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson adapter to handle filterable attributes that may be expressed either as strings or objects.
 */
public class GsonFilterableAttributesRuleTypeAdapter
        extends TypeAdapter<FilterableAttributesRule> {

    @Override
    public void write(JsonWriter writer, FilterableAttributesRule value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }

        if (isSinglePatternWithoutFeatures(value)) {
            writer.value(value.getAttributePatterns()[0]);
            return;
        }

        writer.beginObject();
        writer.name("attributePatterns");
        writeStringArray(writer, value.getAttributePatterns());

        if (value.getFeatures() != null) {
            writer.name("features");
            writer.beginObject();
            if (value.getFeatures().getFacetSearch() != null) {
                writer.name("facetSearch").value(value.getFeatures().getFacetSearch());
            }
            if (value.getFeatures().getFilter() != null) {
                writer.name("filter");
                writer.beginObject();
                if (value.getFeatures().getFilter().getEquality() != null) {
                    writer.name("equality").value(value.getFeatures().getFilter().getEquality());
                }
                if (value.getFeatures().getFilter().getComparison() != null) {
                    writer.name("comparison")
                            .value(value.getFeatures().getFilter().getComparison());
                }
                writer.endObject();
            }
            writer.endObject();
        }

        writer.endObject();
    }

    @Override
    public FilterableAttributesRule read(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        if (token == JsonToken.STRING) {
            return FilterableAttributesRule.fromAttributeName(reader.nextString());
        }

        if (token != JsonToken.BEGIN_OBJECT) {
            reader.skipValue();
            return null;
        }

        FilterableAttributesRule rule = new FilterableAttributesRule();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "attributePatterns":
                    rule.setAttributePatterns(readStringArray(reader));
                    break;
                case "features":
                    rule.setFeatures(readFeatures(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return rule;
    }

    private FilterableAttributesRule.Features readFeatures(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        if (token != JsonToken.BEGIN_OBJECT) {
            reader.skipValue();
            return null;
        }

        FilterableAttributesRule.Features features = new FilterableAttributesRule.Features();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "facetSearch":
                    features.setFacetSearch(readNullableBoolean(reader));
                    break;
                case "filter":
                    features.setFilter(readFilter(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return features;
    }

    private FilterableAttributesRule.Filter readFilter(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        if (token != JsonToken.BEGIN_OBJECT) {
            reader.skipValue();
            return null;
        }

        FilterableAttributesRule.Filter filter = new FilterableAttributesRule.Filter();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "equality":
                    filter.setEquality(readNullableBoolean(reader));
                    break;
                case "comparison":
                    filter.setComparison(readNullableBoolean(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return filter;
    }

    private Boolean readNullableBoolean(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        if (token == JsonToken.BOOLEAN) {
            return reader.nextBoolean();
        }
        reader.skipValue();
        return null;
    }

    private String[] readStringArray(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        if (token == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        if (token == JsonToken.STRING) {
            return new String[] {reader.nextString()};
        }
        if (token != JsonToken.BEGIN_ARRAY) {
            reader.skipValue();
            return null;
        }
        List<String> values = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.STRING) {
                values.add(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endArray();
        return values.toArray(new String[0]);
    }

    private void writeStringArray(JsonWriter writer, String[] values) throws IOException {
        if (values == null) {
            writer.nullValue();
            return;
        }
        writer.beginArray();
        for (String value : values) {
            if (value == null) {
                writer.nullValue();
            } else {
                writer.value(value);
            }
        }
        writer.endArray();
    }

    private boolean isSinglePatternWithoutFeatures(FilterableAttributesRule value) {
        return value.getFeatures() == null
                && value.getAttributePatterns() != null
                && value.getAttributePatterns().length == 1
                && value.getAttributePatterns()[0] != null;
    }
}
