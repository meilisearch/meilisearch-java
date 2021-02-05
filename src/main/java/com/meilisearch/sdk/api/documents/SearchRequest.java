package com.meilisearch.sdk.api.documents;

import java.util.Collections;
import java.util.List;

public class SearchRequest {
	private final String q;
	private final int offset;
	private final int limit;
	private final String filters;
	private final List<String> attributesToRetrieve;
	private final List<String> attributesToCrop;
	private final int cropLength;
	private final List<String> attributesToHighlight;
	private final boolean matches;

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
}
