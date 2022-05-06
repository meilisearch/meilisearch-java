package com.meilisearch.sdk.api.instance;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class IndexStats {
    @Getter @Setter  private long numberOfDocuments;
    @Getter @Setter  private boolean isIndexing;
    @Getter @Setter  private Map<String, Integer> fieldDistribution;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldDistribution) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
    }

}
