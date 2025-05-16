package com.meilisearch.sdk.model;

import java.util.*;
import lombok.Getter;

/**
 * Flattened filtration attributes for the MeiliSearch API See @link <a
 * href="https://meilisearch.notion.site/API-usage-Settings-to-opt-out-indexing-features-filterableAttributes-1764b06b651f80aba8bdf359b2df3ca8?pvs=74">
 * API </a>
 */
@Getter
public class FilterableAttribute {

    String[] patterns;
    Boolean facetSearch;
    Map<String, Boolean> filter;

    public static final String _GEO = "_geo";

    public FilterableAttribute(String pattern) {
        boolean patternIsGeo = _GEO.equals(pattern);
        this.patterns = new String[] {pattern};
        this.facetSearch = patternIsGeo;
        this.filter = new HashMap<>();
        this.filter.put("equality", true);
        this.filter.put("comparison", patternIsGeo);
    }

    public FilterableAttribute(String[] patterns) {
        // Special case of '_geo' pattern will apply special conditions to default attributes
        boolean patternHasGeo = false;
        for (String s : patterns)
            if (_GEO.equals(s)) {
                patternHasGeo = true;
                break;
            }
        this.facetSearch = patternHasGeo;
        this.filter = new HashMap<>();
        this.filter.put("equality", true);
        this.filter.put("comparison", patternHasGeo);
        this.patterns = patterns;
    }

    public FilterableAttribute(
public FilterableAttribute(String[] patterns, boolean facetSearch, Map<String, Boolean> filters) {
    if (patterns == null) throw new IllegalArgumentException("Patterns cannot be null");
    if (filters == null)  throw new IllegalArgumentException("Filters cannot be null");
    boolean patternHasGeo = false;
    for (String s : patterns)
        if (_GEO.equals(s)) {
            patternHasGeo = true;
            break;
        }
    if (patternHasGeo) checkGeoValidation(facetSearch, filters);
    this.patterns  = Arrays.copyOf(patterns, patterns.length); // defensive copy
    this.facetSearch = facetSearch;
    this.filter    = new HashMap<>(filters);                   // defensive copy
}

    private static void checkGeoValidation(boolean facetSearch, Map<String, Boolean> filters) {
        String[] errors = new String[3];
        if (!filters.containsKey("comparison") || filters.get("comparison") == false)
            errors[0] = "Comparison filter cannot be null for '_geo' pattern";
        // rewrite the below lines to be JDK 8 compatible.
        if (!filters.containsKey("equality") || filters.get("equality") == false)
            errors[1] = "Equality filter cannot be null for '_geo' pattern";
        if (!facetSearch) errors[2] = "Facet search cannot be null for '_geo' pattern";
        for (String error : errors)
            if (error != null)
                throw new RuntimeException(
                        "Invalid filter for geo pattern: " + Arrays.toString(errors));
    }
}
