package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Feature toggles for a granular filterable attribute configuration. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterableAttributesFeatures {
    protected Boolean facetSearch;
    protected FilterableAttributesFilter filter;
}
