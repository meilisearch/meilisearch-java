package com.meilisearch.sdk.api.instance;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexStats {
    private long numberOfDocuments;
    private boolean isIndexing;
    private Map<String, Integer> fieldDistribution;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldDistribution) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
    }
}
