package com.meilisearch.sdk.model;

import com.meilisearch.sdk.enums.PrefixSearchSetting;
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
    protected FilterableAttributesConfig[] filterableAttributes;

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
    protected Boolean facetSearch;
    protected PrefixSearchSetting prefixSearch;

    public Settings() {}

    /** Granular filterable attributes accessor. */
    public FilterableAttributesConfig[] getFilterableAttributesConfig() {
        return filterableAttributes;
    }

    public Settings setFilterableAttributesConfig(FilterableAttributesConfig[] configs) {
        this.filterableAttributes = configs;
        return this;
    }

    /** Legacy String[] view of filterable attributes. */
    public String[] getFilterableAttributes() {
        return FilterableAttributesLegacyAdapter.toLegacyNamesOrThrow(filterableAttributes);
    }

    public Settings setFilterableAttributes(String[] filterableAttributes) {
        this.filterableAttributes =
                FilterableAttributesLegacyAdapter.fromLegacyNames(filterableAttributes);
        return this;
    }
}
