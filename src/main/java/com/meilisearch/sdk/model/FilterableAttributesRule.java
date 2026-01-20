package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a filterable attribute rule in Meilisearch v1.14+.
 *
 * <p>A rule can be expressed as a bare string (simple attribute name) or as an object that
 * configures feature toggles (facet search, equality/comparison filters) for a set of attribute
 * patterns.
 */
@Getter
@Setter
@Accessors(chain = true)
public class FilterableAttributesRule {
    private String[] attributePatterns;
    private Features features;

    public FilterableAttributesRule() {}

    public FilterableAttributesRule(String[] attributePatterns, Features features) {
        this.attributePatterns = attributePatterns;
        this.features = features;
    }

    /** Jackson creator to accept both strings and objects. */
    @JsonCreator
    static FilterableAttributesRule fromJson(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isTextual()) {
            return fromAttributeName(node.asText());
        }

        FilterableAttributesRule rule = new FilterableAttributesRule();
        if (node.has("attributePatterns")) {
            rule.attributePatterns = readStringArray(node.get("attributePatterns"));
        }
        if (node.has("features") && node.get("features").isObject()) {
            JsonNode featuresNode = node.get("features");
            Features features = new Features();
            if (featuresNode.has("facetSearch") && !featuresNode.get("facetSearch").isNull()) {
                features.setFacetSearch(featuresNode.get("facetSearch").asBoolean());
            }
            if (featuresNode.has("filter") && featuresNode.get("filter").isObject()) {
                JsonNode filterNode = featuresNode.get("filter");
                Filter filter = new Filter();
                if (filterNode.has("equality") && !filterNode.get("equality").isNull()) {
                    filter.setEquality(filterNode.get("equality").asBoolean());
                }
                if (filterNode.has("comparison") && !filterNode.get("comparison").isNull()) {
                    filter.setComparison(filterNode.get("comparison").asBoolean());
                }
                features.setFilter(filter);
            }
            rule.features = features;
        }
        return rule;
    }

    /** Jackson serializer to emit either a string or an object, mirroring Gson behavior. */
    @JsonValue
    Object toJsonValue() {
        if (isSinglePatternWithoutFeatures()) {
            return attributePatterns[0];
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("attributePatterns", attributePatterns);
        if (features != null) {
            Map<String, Object> featuresMap = new LinkedHashMap<>();
            if (features.getFacetSearch() != null) {
                featuresMap.put("facetSearch", features.getFacetSearch());
            }
            if (features.getFilter() != null) {
                Map<String, Object> filterMap = new LinkedHashMap<>();
                if (features.getFilter().getEquality() != null) {
                    filterMap.put("equality", features.getFilter().getEquality());
                }
                if (features.getFilter().getComparison() != null) {
                    filterMap.put("comparison", features.getFilter().getComparison());
                }
                if (!filterMap.isEmpty()) {
                    featuresMap.put("filter", filterMap);
                }
            }
            if (!featuresMap.isEmpty()) {
                map.put("features", featuresMap);
            }
        }
        return map;
    }

    /** Creates a rule for a single attribute name with no additional feature overrides. */
    public static FilterableAttributesRule fromAttributeName(String attributeName) {
        return new FilterableAttributesRule(new String[] {attributeName}, null);
    }

    /** Creates rules from a list of attribute names, one rule per attribute. */
    public static FilterableAttributesRule[] fromAttributeNames(String[] attributeNames) {
        if (attributeNames == null) {
            return null;
        }
        FilterableAttributesRule[] rules = new FilterableAttributesRule[attributeNames.length];
        for (int i = 0; i < attributeNames.length; i++) {
            rules[i] = fromAttributeName(attributeNames[i]);
        }
        return rules;
    }

    /** Flattens a set of rules into a string view of attribute patterns. */
    public static String[] toAttributeNamesView(FilterableAttributesRule[] rules) {
        if (rules == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (FilterableAttributesRule rule : rules) {
            if (rule == null || rule.getAttributePatterns() == null) {
                continue;
            }
            for (String pattern : rule.getAttributePatterns()) {
                if (pattern != null) {
                    names.add(pattern);
                }
            }
        }
        return names.toArray(new String[0]);
    }

    private boolean isSinglePatternWithoutFeatures() {
        return features == null
                && attributePatterns != null
                && attributePatterns.length == 1
                && attributePatterns[0] != null;
    }

    private static String[] readStringArray(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return new String[] {node.asText()};
        }
        if (!node.isArray()) {
            return null;
        }
        List<String> values = new ArrayList<>();
        for (JsonNode child : node) {
            if (child.isTextual()) {
                values.add(child.asText());
            }
        }
        return values.toArray(new String[0]);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Features {
        private Boolean facetSearch;
        private Filter filter;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Filter {
        private Boolean equality;
        private Boolean comparison;
    }
}
