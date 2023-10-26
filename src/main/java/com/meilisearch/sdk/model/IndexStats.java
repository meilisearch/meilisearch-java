package com.meilisearch.sdk.model;

import java.util.Map;
import lombok.Getter;

/**
 * Stats data structure of a Meilisearch Index
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats#get-stats-of-an-index">API
 *     specification</a>
 */
@Getter
public class IndexStats {
    protected long numberOfDocuments;
    protected boolean isIndexing;
    protected Map<String, Integer> fieldDistribution;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldDistribution) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
    }
}
