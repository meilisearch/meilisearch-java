package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

/**
 * Enum for Sorting Facet Values
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#faceting-object">API
 *     specification</a>
 */
public enum FacetSortValue {
    @SerializedName("alpha")
    ALPHA("alpha"),
    @SerializedName("count")
    COUNT("count");

    public final String facetSortValue;

    FacetSortValue(String facetSortValue) {
        this.facetSortValue = facetSortValue;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.facetSortValue;
    }
}
