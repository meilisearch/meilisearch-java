package com.meilisearch.sdk;

import org.json.JSONObject;

/** Search request query string builder */
public class SearchRequest {
    private String q;
    private int offset;
    private int limit;
    private String[] attributesToRetrieve;
    private String[] attributesToCrop;
    private int cropLength;
    private String[] attributesToHighlight;
    private String filter;
    private boolean matches;
    private String[] facetsDistribution;
    private String[] sort;

    /** Empty SearchRequest constructor */
    public SearchRequest() {}

    /**
     * Constructor for SearchRequest for building search queries with the default values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, matches: false, facetsDistribution: null, sort:
     * null
     *
     * @param q Query String
     */
    public SearchRequest(String q) {
        this(q, 0);
    }

    /**
     * Constructor for SearchRequest for building search queries with the default values: limit: 20,
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200, attributesToHighlight:
     * null, filter: null, matches: false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     */
    public SearchRequest(String q, int offset) {
        this(q, offset, 20);
    }

    /**
     * Constructor for SearchRequest for building search queries with the default values:
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200, attributesToHighlight:
     * null, filter: null, matches: false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     */
    public SearchRequest(String q, int offset, int limit) {
        this(q, offset, limit, new String[] {"*"});
    }

    /**
     * Constructor for SearchRequest for building search queries with the default values:
     * attributesToCrop: null, cropLength: 200, attributesToHighlight: null, filter: null, matches:
     * false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     */
    public SearchRequest(String q, int offset, int limit, String[] attributesToRetrieve) {
        this(q, offset, limit, attributesToRetrieve, null, 200, null, null, false, null, null);
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
     * @param filter Filter queries by an attribute value
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     */
    public SearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String[] attributesToHighlight,
            String filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this.q = q;
        this.offset = offset;
        this.limit = limit;
        this.attributesToRetrieve = attributesToRetrieve;
        this.attributesToCrop = attributesToCrop;
        this.cropLength = cropLength;
        this.attributesToHighlight = attributesToHighlight;
        this.setFilter(filter);
        this.matches = matches;
        this.facetsDistribution = facetsDistribution;
        this.sort = sort;
    }

    /**
     * Method for returning the Query String
     *
     * @return query String
     */
    public String getQ() {
        return q;
    }

    /**
     * Method for returning the offset
     *
     * @return number of documents to skip
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Method for returning the limit
     *
     * @return maximum number of documents returned
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Method for returning the attributesToRetrieve
     *
     * @return attributes whose values will contain highlighted matching terms
     */
    public String[] getAttributesToRetrieve() {
        return attributesToRetrieve;
    }

    /**
     * Method for returning the attributesToCrop
     *
     * @return attributes whose values have to be cropped
     */
    public String[] getAttributesToCrop() {
        return attributesToCrop;
    }

    /**
     * Method for returning the cropLength
     *
     * @return length used to crop field values
     */
    public int getCropLength() {
        return cropLength;
    }

    /**
     * Method for returning the attributesToHighlight
     *
     * @return attributes whose values will contain highlighted matching terms
     */
    public String[] getAttributesToHighlight() {
        return attributesToHighlight;
    }

    /**
     * Method to return the filter
     *
     * @return filter queries by an attribute value
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Method to return the matches
     *
     * @return defines whether an object that contains information about the matches should be
     *     returned or not
     */
    public boolean getMatches() {
        return matches;
    }

    /**
     * Method for returning the facetsDistribution
     *
     * @return facets for which to retrieve the matching count
     */
    public String[] getFacetsDistribution() {
        return facetsDistribution;
    }

    /**
     * Method for returning the sort
     *
     * @return Sort queries by an attribute value
     */
    public String[] getSort() {
        return sort;
    }

    /**
     * Method to set the Query String
     *
     * @param q Query String
     * @return altered SearchRequest
     */
    public SearchRequest setQ(String q) {
        this.q = q;
        return this;
    }

    /**
     * Method to set the offset
     *
     * @param offset Number of documents to skip
     * @return altered SearchRequest
     */
    public SearchRequest setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Method to set the limit
     *
     * @param limit Maximum number of documents returned
     * @return altered SearchRequest
     */
    public SearchRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Method to set the attributesToRetrieve
     *
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @return altered SearchRequest
     */
    public SearchRequest setAttributesToRetrieve(String[] attributesToRetrieve) {
        this.attributesToRetrieve = attributesToRetrieve;
        return this;
    }

    /**
     * Method to set the attributesToCrop
     *
     * @param attributesToCrop Attributes whose values have been cropped
     * @return altered SearchRequest
     */
    public SearchRequest setAttributesToCrop(String[] attributesToCrop) {
        this.attributesToCrop = attributesToCrop;
        return this;
    }

    /**
     * Method to set the cropLength
     *
     * @param cropLength Length used to crop field values
     * @return altered SearchRequest
     */
    public SearchRequest setCropLength(int cropLength) {
        this.cropLength = cropLength;
        return this;
    }

    /**
     * Method to set the attributesToHighlight
     *
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @return altered SearchRequest
     */
    public SearchRequest setAttributesToHighlight(String[] attributesToHighlight) {
        this.attributesToHighlight = attributesToHighlight;
        return this;
    }

    /**
     * Method to set the filter
     *
     * @param filter Filter queries by an attribute value
     * @return altered SearchRequest
     */
    public SearchRequest setFilter(String filter) {
        if (filter != null) {
            this.filter = filter;
        }
        return this;
    }

    /**
     * Method to set the matches boolean
     *
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @return altered SearchRequest
     */
    public SearchRequest setMatches(boolean matches) {
        this.matches = matches;
        return this;
    }

    /**
     * Method to set the facetsDistribution
     *
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @return altered SearchRequest
     */
    public SearchRequest setFacetsDistribution(String[] facetsDistribution) {
        this.facetsDistribution = facetsDistribution;
        return this;
    }

    /**
     * Method to set the sort
     *
     * @param sort Sort queries by an attribute value
     * @return altered SearchRequest
     */
    public SearchRequest setSort(String[] sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Method that returns the JSON String of the SearchRequest
     *
     * @return JSON String of the SearchRequest query
     */
    String getQuery() {

        JSONObject jsonObject =
                new JSONObject()
                        .put("q", this.q)
                        .put("offset", this.offset)
                        .put("limit", this.limit)
                        .put("attributesToRetrieve", this.attributesToRetrieve)
                        .put("cropLength", this.cropLength)
                        .put("matches", this.matches)
                        .put("facetsDistribution", this.facetsDistribution)
                        .put("sort", this.sort);
        if (this.attributesToCrop != null) {
            jsonObject.put("attributesToCrop", this.attributesToCrop);
        }
        if (this.attributesToHighlight != null) {
            jsonObject.put("attributesToHighlight", this.attributesToHighlight);
        }
        if (this.filter != null) {
            jsonObject.put("filter", this.filter);
        }
        return jsonObject.toString();
    }
}
