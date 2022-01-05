package com.meilisearch.sdk.api.instance;

import java.util.Map;

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

    public long getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(long numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public boolean isIndexing() {
        return isIndexing;
    }

    public void setIndexing(boolean indexing) {
        isIndexing = indexing;
    }

    public Map<String, Integer> getFieldDistribution() {
        return fieldDistribution;
    }

    public void setFieldDistribution(Map<String, Integer> fieldDistribution) {
        this.fieldDistribution = fieldDistribution;
    }
}
