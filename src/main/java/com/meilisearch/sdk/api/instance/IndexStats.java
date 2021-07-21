package com.meilisearch.sdk.api.instance;

import java.util.Map;

public class IndexStats {
    private int numberOfDocuments;
    private boolean isIndexing;
    private Map<String, Integer> fieldDistribution;

    public IndexStats() {}

    public IndexStats(
            int numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldDistribution) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(int numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public boolean isIndexing() {
        return isIndexing;
    }

    public void setIndexing(boolean indexing) {
        isIndexing = indexing;
    }

    public Map<String, Integer> getFieldsDistribution() {
        return fieldDistribution;
    }

    public void setFieldsDistribution(Map<String, Integer> fieldDistribution) {
        this.fieldDistribution = fieldDistribution;
    }
}
