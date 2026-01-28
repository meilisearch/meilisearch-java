package com.meilisearch.sdk.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.FilterableAttributesFeatures;
import com.meilisearch.sdk.model.FilterableAttributesFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Jackson deserializer for polymorphic filterable attributes config (string or object). */
public class JacksonFilterableAttributesConfigDeserializer
        extends JsonDeserializer<FilterableAttributesConfig> {

    @Override
    public FilterableAttributesConfig deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isTextual()) {
            return FilterableAttributesConfig.simple(node.asText());
        }

        FilterableAttributesConfig config = new FilterableAttributesConfig();
        JsonNode patternsNode = node.get("attributePatterns");
        if (patternsNode != null && patternsNode.isArray()) {
            List<String> patterns = new ArrayList<>();
            for (JsonNode patternNode : patternsNode) {
                patterns.add(patternNode.isNull() ? null : patternNode.asText());
            }
            config.setAttributePatterns(patterns.toArray(new String[0]));
        }

        JsonNode featuresNode = node.get("features");
        if (featuresNode != null && featuresNode.isObject()) {
            config.setFeatures(parseFeatures(featuresNode));
        }

        return config;
    }

    private FilterableAttributesFeatures parseFeatures(JsonNode featuresNode) {
        FilterableAttributesFeatures features = new FilterableAttributesFeatures();
        JsonNode facetSearchNode = featuresNode.get("facetSearch");
        if (facetSearchNode != null && !facetSearchNode.isNull()) {
            features.setFacetSearch(facetSearchNode.asBoolean());
        }

        JsonNode filterNode = featuresNode.get("filter");
        if (filterNode != null && filterNode.isObject()) {
            FilterableAttributesFilter filter = new FilterableAttributesFilter();
            JsonNode equalityNode = filterNode.get("equality");
            if (equalityNode != null && !equalityNode.isNull()) {
                filter.setEquality(equalityNode.asBoolean());
            }
            JsonNode comparisonNode = filterNode.get("comparison");
            if (comparisonNode != null && !comparisonNode.isNull()) {
                filter.setComparison(comparisonNode.asBoolean());
            }
            features.setFilter(filter);
        }

        return features;
    }
}
