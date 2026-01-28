package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Granular filterable attribute configuration (Meilisearch v1.14+).
 *
 * <p>Represents either a simple attribute name or a structured configuration that can
 * disable/enable specific filter features.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterableAttributesConfig {
    protected String[] attributePatterns;
    protected FilterableAttributesFeatures features;

    /**
     * Convenience factory for the simple string form of a filterable attribute.
     *
     * <p>Equivalent to adding the attribute directly to the {@code filterableAttributes}
     * array in the Meilisearch settings (e.g., {@code "filterableAttributes": ["genres"]}).
     * The string form enables all filter types for the given attribute.
     */
    public static FilterableAttributesConfig simple(String name) {
        FilterableAttributesConfig config = new FilterableAttributesConfig();
        config.setAttributePatterns(new String[] {name});
        return config;
    }
}
