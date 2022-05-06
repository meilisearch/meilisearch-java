package com.meilisearch.sdk;

import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

/** Search request query string builder */
public class SearchRequest {
    @Getter @Setter private String q;
    @Getter @Setter private int offset;
    @Getter @Setter private int limit;
    @Getter @Setter private String[] attributesToRetrieve;
    @Getter @Setter private String[] attributesToCrop;
    @Getter @Setter private int cropLength;
    @Getter @Setter private String[] attributesToHighlight;
    @Getter @Setter private String[] filter;
    @Getter @Setter private String[][] filterArray;
    @Getter @Setter private boolean matches;
    @Getter @Setter private String[] facetsDistribution;
    @Getter @Setter private String[] sort;

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
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                null,
                200,
                null,
                (String[]) null,
                false,
                null,
                null);
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
            String[] filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                attributesToHighlight,
                filter,
                null,
                matches,
                facetsDistribution,
                sort);
    }

    /**
     * Full SearchRequest Constructor for building search queries with 2D filter Array
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filterArray String array that can take multiple nested filters
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
            String[][] filterArray,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                attributesToHighlight,
                null,
                filterArray,
                matches,
                facetsDistribution,
                sort);
    }

    private SearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String[] attributesToHighlight,
            String[] filter,
            String[][] filterArray,
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
        this.setFilterArray(filterArray);
        this.matches = matches;
        this.facetsDistribution = facetsDistribution;
        this.sort = sort;
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
        if (this.filterArray != null) {
            jsonObject.put("filter", this.filterArray);
        }
        return jsonObject.toString();
    }
}
