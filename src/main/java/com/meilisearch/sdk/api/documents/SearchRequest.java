package com.meilisearch.sdk.api.documents;

import java.util.Collections;
import java.util.List;

public class SearchRequest {
	private String q;
	private int offset;
	private int limit;
	private String filters;
	private List<String> attributesToRetrieve;
	private List<String> attributesToCrop;
	private int cropLength;
	private List<String> attributesToHighlight;
	private boolean matches;

	public SearchRequest(String q) {
		this(q, 0);
	}

	public SearchRequest(String q, int offset) {
		this(q, offset, 20);
	}

	public SearchRequest(String q, int offset, int limit) {
		this(q, offset, limit, Collections.singletonList("*"));
	}

	public SearchRequest(String q, int offset, int limit, List<String> attributesToRetrieve) {
		this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false);
	}

	public SearchRequest(String q,
						 int offset,
						 int limit,
						 List<String> attributesToRetrieve,
						 List<String> attributesToCrop,
						 int cropLength,
						 List<String> attributesToHighlight,
						 String filters,
						 boolean matches) {
		this.q = q;
		this.offset = offset;
		this.limit = limit;
		this.attributesToRetrieve = attributesToRetrieve;
		this.attributesToCrop = attributesToCrop;
		this.cropLength = cropLength;
		this.attributesToHighlight = attributesToHighlight;
		this.filters = filters;
		this.matches = matches;
	}

	public String getQ() {
		return q;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public List<String> getAttributesToRetrieve() {
		return attributesToRetrieve;
	}

	public List<String> getAttributesToCrop() {
		return attributesToCrop;
	}

	public int getCropLength() {
		return cropLength;
	}

	public List<String> getAttributesToHighlight() {
		return attributesToHighlight;
	}

	public String getFilters() {
		return filters;
	}

	public boolean isMatches() {
		return matches;
	}

	public SearchRequest setQ(String q) {
		this.q = q;
		return this;
	}

	public SearchRequest setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public SearchRequest setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public SearchRequest setFilters(String filters) {
		this.filters = filters;
		return this;
	}

	public SearchRequest setAttributesToRetrieve(List<String> attributesToRetrieve) {
		this.attributesToRetrieve = attributesToRetrieve;
		return this;
	}

	public SearchRequest setAttributesToCrop(List<String> attributesToCrop) {
		this.attributesToCrop = attributesToCrop;
		return this;
	}

	public SearchRequest setCropLength(int cropLength) {
		this.cropLength = cropLength;
		return this;
	}

	public SearchRequest setAttributesToHighlight(List<String> attributesToHighlight) {
		this.attributesToHighlight = attributesToHighlight;
		return this;
	}

	public SearchRequest setMatches(boolean matches) {
		this.matches = matches;
		return this;
	}
}
