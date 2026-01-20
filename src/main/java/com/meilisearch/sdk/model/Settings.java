package com.meilisearch.sdk.model;

import com.meilisearch.sdk.model.FilterableAttributesRule;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Meilisearch settings data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings">API specification</a>
 */
@Getter
@Setter
@Accessors(chain = true)
public class Settings {

    protected HashMap<String, String[]> synonyms;
    protected String[] stopWords;
    protected String[] rankingRules;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected FilterableAttributesRule[] filterableAttributes;
    protected String distinctAttribute;
    protected String[] searchableAttributes;
    protected String[] displayedAttributes;
    protected String[] sortableAttributes;
    protected TypoTolerance typoTolerance;
    protected Pagination pagination;
    protected Faceting faceting;
    protected String[] dictionary;
    protected String proximityPrecision;
    protected Integer searchCutoffMs;
    protected String[] separatorTokens;
    protected String[] nonSeparatorTokens;
    protected HashMap<String, Embedder> embedders;
    protected LocalizedAttribute[] localizedAttributes;

    public Settings() {}

    /**
     * Returns the advanced filterable attributes configuration (v1.14+).
     *
     * @return array of FilterableAttributesRule or null
     */
    public FilterableAttributesRule[] getFilterableAttributesConfig() {
        return filterableAttributes;
    }

    /**
     * Sets the advanced filterable attributes configuration (v1.14+).
     *
     * @param rules array of FilterableAttributesRule
     * @return this Settings instance
     */
    public Settings setFilterableAttributesConfig(FilterableAttributesRule[] rules) {
        this.filterableAttributes = rules;
        return this;
    }

    /**
     * Legacy string-only getter for filterable attributes.
     *
     * <p>Returns a flattened view of the configured attribute patterns.
     *
     * @return array of attribute names/patterns or null
     */
    public String[] getFilterableAttributes() {
        return FilterableAttributesRule.toAttributeNamesView(this.filterableAttributes);
    }

    /**
     * Legacy string-only setter for filterable attributes.
     *
     * <p>Internally maps each attribute name to a rule with a single attribute pattern and no custom
     * feature toggles.
     *
     * @param attributes array of attribute names/patterns
     * @return this Settings instance
     */
    public Settings setFilterableAttributes(String[] attributes) {
        this.filterableAttributes = FilterableAttributesRule.fromAttributeNames(attributes);
        return this;
    }
}
