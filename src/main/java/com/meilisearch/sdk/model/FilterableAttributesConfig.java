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

    /** Convenience factory for legacy single-attribute usage. */
    public static FilterableAttributesConfig fromAttributeName(String name) {
        FilterableAttributesConfig config = new FilterableAttributesConfig();
        config.setAttributePatterns(new String[] {name});
        return config;
    }
}
