package com.meilisearch.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

/**
 * Search request query string builder
 */
public class SearchRequest {
	private String q;
	private int offset;
	private int limit;
	private String[] attributesToRetrieve;
	private String[] attributesToCrop;
	private int cropLength;
	private String[] attributesToHighlight;
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
		this(q, offset, limit, new String[]{"*"});
	}

	public SearchRequest(String q, int offset, int limit, String[] attributesToRetrieve) {
		this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false);
	}

	public SearchRequest(String q,
				  int offset,
				  int limit,
				  String[] attributesToRetrieve,
				  String[] attributesToCrop,
				  int cropLength,
				  String[] attributesToHighlight,
				  String filters,
				  boolean matches) {
		this.q = q;
		this.offset = offset;
		this.limit = limit;
		this.attributesToRetrieve = attributesToRetrieve;
		this.attributesToCrop = attributesToCrop;
		this.cropLength = cropLength;
		this.attributesToHighlight = attributesToHighlight;
		this.setFilters(filters);
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

	public String[] getAttributesToRetrieve() {
		return attributesToRetrieve;
	}

	public String[] getAttributesToCrop() {
		return attributesToCrop;
	}

	public int getCropLength() {
		return cropLength;
	}

	public String[] getAttributesToHighlight() {
		return attributesToHighlight;
	}

	public String getFilters() {
		return filters;
	}

	public boolean getMatches() {
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

	public SearchRequest setAttributesToRetrieve(String[] attributesToRetrieve) {
		this.attributesToRetrieve = attributesToRetrieve;
		return this;
	}

	public SearchRequest setAttributesToCrop(String[] attributesToCrop) {
		this.attributesToCrop = attributesToCrop;
		return this;
	}

	public SearchRequest setCropLength(int cropLength) {
		this.cropLength = cropLength;
		return this;
	}

	public SearchRequest setAttributesToHighlight(String[] attributesToHighlight) {
		this.attributesToHighlight = attributesToHighlight;
		return this;
	}

	public SearchRequest setFilters(String filters) {
		if (filters != null) {
			this.filters = filters;// URLEncoder.encode(filters, StandardCharsets.UTF_8.toString())
		}
		return this;
	}

	public SearchRequest setMatches(boolean matches) {
		this.matches=matches;
		return this;
	}

	String getQuery() {

		JSONObject jsonObject = new JSONObject()
		.put("q", this.q)
		.put("offset",this.offset)
		.put("limit", this.limit)
		.put("attributesToRetrieve", this.attributesToRetrieve)
		.put("cropLength",this.cropLength)
		.put("matches", this.matches);
		if (this.attributesToCrop != null) {
			jsonObject.put("attributesToCrop",this.attributesToCrop);
		}
		if (this.attributesToHighlight != null) {
			jsonObject.put("attributesToHighlight",this.attributesToHighlight);
		}
		if (this.filters != null) {
			jsonObject.put("filters",this.filters);
		}
		return jsonObject.toString();
	}
}
