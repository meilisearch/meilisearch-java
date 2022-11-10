package com.meilisearch.sdk.model;

import java.util.Map;
import lombok.Getter;

/**
 * Data structure of Meilisearch response for the Stats of an Index
 *
 * <p>https://docs.meilisearch.com/reference/api/stats.html#get-stats-of-an-index
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
