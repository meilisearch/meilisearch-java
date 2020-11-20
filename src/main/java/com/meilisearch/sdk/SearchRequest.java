package com.meilisearch.sdk;

/**
 * Search request query string builder
 */
public class SearchRequest {
	private String q;
	private int offset;
	private int limit;
	private String attributesToRetrieve;
	private String attributesToCrop;
	private int cropLength;
	private String attributesToHighlight;
	private String filters;
	private boolean matches;

	public SearchRequest() {
	}

	public SearchRequest(String q) {
		this(q, 0);
	}

	public SearchRequest(String q, int offset) {
		this(q, offset, 20);
	}

	public SearchRequest(String q, int offset, int limit) {
		this(q, offset, limit, "*");
	}

	public SearchRequest(String q, int offset, int limit, String attributesToRetrieve) {
		this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false);
	}

	public SearchRequest(String q,
				  int offset,
				  int limit,
				  String attributesToRetrieve,
				  String attributesToCrop,
				  int cropLength,
				  String attributesToHighlight,
				  String filters,
				  boolean matches) {
		this.q = q.replaceAll("\\s+?", "%20");
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

	public String getAttributesToRetrieve() {
		return attributesToRetrieve;
	}

	public String getAttributesToCrop() {
		return attributesToCrop;
	}

	public int getCropLength() {
		return cropLength;
	}

	public String getAttributesToHighlight() {
		return attributesToHighlight;
	}

	public String getFilters() {
		return filters;
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

	

	public boolean isMatches() {
		return matches;
	}

	String getQuery() {
		StringBuilder sb = new StringBuilder();

		// Default parameters
		sb.append("?q=").append(this.q)
			.append("&offset=").append(this.offset)
			.append("&limit=").append(this.limit)
			.append("&attributesToRetrieve=").append(this.attributesToRetrieve)
			.append("&cropLength=").append(this.cropLength)
			.append("&matches=").append(this.matches);

		if (this.attributesToCrop != null) {
			sb.append("&attributesToCrop=").append(this.attributesToCrop);
		}

		if (this.attributesToHighlight != null) {
			sb.append("&attributesToHighlight=").append(this.attributesToHighlight);
		}

		if (this.filters != null) {
			sb.append("&filters=").append(this.filters);
		}

		return sb.toString();
	}
}
