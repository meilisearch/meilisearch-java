package com.meilisearch.sdk;

import org.json.JSONObject;

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

	/**
	 * Empty SearchRequest constructor
	 */
	public SearchRequest() {
	}

	/**
	 * Constructor for SearchRequest for building search queries
	 * with the default values:
	 * offset: 0, limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null,
	 * cropLength: 200, attributesToHighlight: null, filters: null, matches: false
	 *
	 * @param q Query String
	 */
	public SearchRequest(String q) {
		this(q, 0);
	}

	/**
	 * Constructor for SearchRequest for building search queries
	 * with the default values:
	 * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
	 * attributesToHighlight: null, filters: null, matches: false
	 *
	 * @param q Query String
	 * @param offset Number of documents to skip
	 */
	public SearchRequest(String q, int offset) {
		this(q, offset, 20);
	}

	/**
	 * Constructor for SearchRequest for building search queries
	 * with the default values:
	 * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200, attributesToHighlight: null,
	 * filters: null, matches: false
	 *
	 * @param q Query String
	 * @param offset Number of documents to skip
	 * @param limit Maximum number of documents returned
	 */
	public SearchRequest(String q, int offset, int limit) {
		this(q, offset, limit, new String[]{"*"});
	}

	/**
	 * Constructor for SearchRequest for building search queries
	 * with the default values:
	 * attributesToCrop: null, cropLength: 200, attributesToHighlight: null, filters: null, matches: false
	 *
	 * @param q Query String
	 * @param offset Number of documents to skip
	 * @param limit Maximum number of documents returned
	 * @param attributesToRetrieve Attributes to display in the returned documents
	 */
	public SearchRequest(String q, int offset, int limit, String[] attributesToRetrieve) {
		this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false);
	}

	/**
	 * Full SearchRequest Constructor for building search queries
	 *
	 * @param q Query string
	 * @param offset Number of documents to skip
	 * @param limit Maximum number of documents returned
	 * @param attributesToRetrieve Attributes to display in the returned documents
	 * @param attributesToCrop Attributes whose values have been cropped
	 * @param cropLength Length used to crop field values
	 * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
	 * @param filters Filter queries by an attribute value
	 * @param matches Defines whether an object that contains information about the matches should be returned or not
	 */
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

	/**
	 * Method for returning the Query String
	 * @return Query String
	 */
	public String getQ() {
		return q;
	}

	/**
	 * Method for returning the offset
	 * @return Number of documents to skip
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Method for returning the limit
	 * @return Maximum number of documents returned
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Method for returning the attributesToRetrieve
	 * @return Attributes whose values will contain highlighted matching terms
	 */
	public String[] getAttributesToRetrieve() {
		return attributesToRetrieve;
	}

	/**
	 * Method for returning the attributesToCrop
	 * @return Attributes whose values have to be cropped
	 */
	public String[] getAttributesToCrop() {
		return attributesToCrop;
	}

	/**
	 * Method for returning the cropLength
	 * @return Length used to crop field values
	 */
	public int getCropLength() {
		return cropLength;
	}

	/**
	 * Method for returning the attributesToHighlight
	 * @return Attributes whose values will contain highlighted matching terms
	 */
	public String[] getAttributesToHighlight() {
		return attributesToHighlight;
	}

	/**
	 * Method to return the filters
	 * @return Filter queries by an attribute value
	 */
	public String getFilters() {
		return filters;
	}

	/**
	 * Method to return the matches
	 * @return Defines whether an object that contains information about the matches should be returned or not
	 */
	public boolean getMatches() {
		return matches;
	}

	/**
	 * Method to set the Query String
	 * @param q Query String
	 * @return altered SearchRequest
	 */
	public SearchRequest setQ(String q) {
		this.q = q;
		return this;
	}

	/**
	 * Method to set the offset
	 * @param offset Number of documents to skip
	 * @return altered SearchRequest
	 */
	public SearchRequest setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Method to set the limit
	 * @param limit Maximum number of documents returned
	 * @return altered SearchRequest
	 */
	public SearchRequest setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Method to set the attributesToRetrieve
	 * @param attributesToRetrieve Attributes to display in the returned documents
	 * @return altered SearchRequest
	 */
	public SearchRequest setAttributesToRetrieve(String[] attributesToRetrieve) {
		this.attributesToRetrieve = attributesToRetrieve;
		return this;
	}

	/**
	 * Method to set the attributesToCrop
	 * @param attributesToCrop Attributes whose values have been cropped
	 * @return altered SearchRequest
	 */
	public SearchRequest setAttributesToCrop(String[] attributesToCrop) {
		this.attributesToCrop = attributesToCrop;
		return this;
	}

	/**
	 * Method to set the cropLength
	 * @param cropLength Length used to crop field values
	 * @return altered SearchRequest
	 */
	public SearchRequest setCropLength(int cropLength) {
		this.cropLength = cropLength;
		return this;
	}

	/**
	 * Method to set the attributesToHighlight
	 * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
	 * @return altered SearchRequest
	 */
	public SearchRequest setAttributesToHighlight(String[] attributesToHighlight) {
		this.attributesToHighlight = attributesToHighlight;
		return this;
	}

	/**
	 * Method to set the filters
	 * @param filters Filter queries by an attribute value
	 * @return altered SearchRequest
	 */
	public SearchRequest setFilters(String filters) {
		if (filters != null) {
			this.filters = filters;
		}
		return this;
	}

	/**
	 * Method to set the matches boolean
	 * @param matches Defines whether an object that contains information about the matches should be returned or not
	 * @return altered SearchRequest
	 */
	public SearchRequest setMatches(boolean matches) {
		this.matches=matches;
		return this;
	}

	/**
	 * Method that returns the JSON String of the SearchRequest
	 * @return JSON String of the SearchRequest query
	 */
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
