package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ForeignKey setting data structure for cross-index document hydration.
 *
 * <p>Each ForeignKey entry links a field in the current index to a field in another index, allowing
 * search results to be automatically enriched with related data.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings/get-foreignkeys">API
 *     specification</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForeignKey {

    // The uid of the foreign (related) index this entry points to.
    protected String foreignIndexUid;

    // The name of the field in the current index that holds the foreign key value.
    protected String fieldName;
}
