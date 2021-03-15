package com.meilisearch.sdk.api.documents;

import java.util.List;

public class SearchResponse<T> {
	private List<T> hits;
	private int offset;
	private int limit;
	private int nbHits;
	private boolean exhaustiveNbHits;
	private int processingTimeMs;
	private String query;

	public List<T> getHits() {
		return hits;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public int getNbHits() {
		return nbHits;
	}

	public boolean isExhaustiveNbHits() {
		return exhaustiveNbHits;
	}

	public int getProcessingTimeMs() {
		return processingTimeMs;
	}

	public String getQuery() {
		return query;
	}
}
