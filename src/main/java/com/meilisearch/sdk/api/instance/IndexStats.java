package com.meilisearch.sdk.api.instance;

import java.util.Map;

public class IndexStats {
	private int numberOfDocuments;
	private boolean isIndexing;
	private Map<String,Integer> fieldsDistribution;

	public IndexStats() {
	}

	public IndexStats(int numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldsDistribution) {
		this.numberOfDocuments = numberOfDocuments;
		this.isIndexing = isIndexing;
		this.fieldsDistribution = fieldsDistribution;
	}

	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}

	public void setNumberOfDocuments(int numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}

	public boolean getIsIndexing() {
		return isIndexing;
	}

	public void setIsIndexing(boolean indexing) {
		isIndexing = indexing;
	}

	public Map<String, Integer> getFieldsDistribution() {
		return fieldsDistribution;
	}

	public void setFieldsDistribution(Map<String, Integer> fieldsDistribution) {
		this.fieldsDistribution = fieldsDistribution;
	}
}
