package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Filter toggles for granular filterable attributes.
 *
 * <p>Allows enabling or disabling equality and comparison filters independently.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterableAttributesFilter {
    protected Boolean equality;
    protected Boolean comparison;
}
