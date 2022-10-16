package com.meilisearch.sdk;

import lombok.Getter;
import org.json.JSONObject;

/** Search request query string builder */
@Getter
public class SearchRequest {
    private String q;
    private int offset;
    private int limit;
    private String[] attributesToRetrieve;
    private String[] attributesToCrop;
    private int cropLength;
    private String cropMarker;
    private String highlightPreTag;
    private String highlightPostTag;
    private String[] attributesToHighlight;
    private String[] filter;
    private String[][] filterArray;
    private boolean showMatchesPosition;
    private String[] facets;
    private String[] sort;

    /** Empty SearchRequest constructor */
    public SearchRequest() {
    }

    /**
     * Constructor for SearchRequest for building search queries with the default
     * values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength:
     * 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false,
     * facets: null, sort: null
     *
     * @param q Query String
     */
    public SearchRequest(String q) {
        this(q, 0);
    }

    /**
     * Constructor for SearchRequest for building search queries with the default
     * values: limit: 20,
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false,
     * facets: null, sort: null
     *
     * @param q      Query String
     * @param offset Number of documents to skip
     */
    public SearchRequest(String q, int offset) {
        this(q, offset, 20);
    }

    /**
     * Constructor for SearchRequest for building search queries with the default
     * values:
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false,
     * facets: null, sort: null
     *
     * @param q      Query String
     * @param offset Number of documents to skip
     * @param limit  Maximum number of documents returned
     */
    public SearchRequest(String q, int offset, int limit) {
        this(q, offset, limit, new String[] { "*" });
    }

    /**
     * Constructor for SearchRequest for building search queries with the default
     * values:
     * attributesToCrop: null, cropLength: 200, attributesToHighlight: null, filter:
     * null,
     * showMatchesPosition: false, facets: null, sort: null
     *
     * @param q                    Query String
     * @param offset               Number of documents to skip
     * @param limit                Maximum number of documents returned
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
                null,
                null,
                null,
                (String[]) null,
                false,
                null,
                null);
    }

    /**
     * Full SearchRequest Constructor for building search queries
     *
     * @param q                     Query string
     * @param offset                Number of documents to skip
     * @param limit                 Maximum number of documents returned
     * @param attributesToRetrieve  Attributes to display in the returned documents
     * @param attributesToCrop      Attributes whose values have been cropped
     * @param cropLength            Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted
     *                              matching terms
     * @param filter                Filter queries by an attribute value
     * @param showMatchesPosition   Defines whether an object that contains
     *                              information about the showMatchesPosition should
     *                              be returned or not
     * @param facets                Facets for which to retrieve the matching count
     * @param sort                  Sort queries by an attribute value
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
            boolean showMatchesPosition,
            String[] facets,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                null,
                null,
                null,
                attributesToHighlight,
                filter,
                null,
                showMatchesPosition,
                facets,
                sort);
    }

    /**
     * Full SearchRequest Constructor for building search queries
     *
     * @param q                     Query string
     * @param offset                Number of documents to skip
     * @param limit                 Maximum number of documents returned
     * @param attributesToRetrieve  Attributes to display in the returned documents
     * @param attributesToCrop      Attributes whose values have been cropped
     * @param cropLength            Length used to crop field values
     * @param cropMarker            String to customize default crop marker, default
     *                              value: …
     * @param highlightPreTag       String to customize highlight tag before every
     *                              highlighted query
     *                              terms, default value: <em>
     * @param highlightPostTag      String to customize highlight tag after every
     *                              highlighted query
     *                              terms, default value: </em>
     * @param attributesToHighlight Attributes whose values will contain highlighted
     *                              matching terms
     * @param filter                Filter queries by an attribute value
     * @param showMatchesPosition   Defines whether an object that contains
     *                              information about the showMatchesPosition should
     *                              be returned or not
     * @param facets                Facets for which to retrieve the matching count
     * @param sort                  Sort queries by an attribute value
     */
    public SearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            boolean showMatchesPosition,
            String[] facets,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                cropMarker,
                highlightPreTag,
                highlightPostTag,
                attributesToHighlight,
                filter,
                null,
                showMatchesPosition,
                facets,
                sort);
    }

    /**
     * Full SearchRequest Constructor for building search queries with 2D filter
     * Array
     *
     * @param q                     Query string
     * @param offset                Number of documents to skip
     * @param limit                 Maximum number of documents returned
     * @param attributesToRetrieve  Attributes to display in the returned documents
     * @param attributesToCrop      Attributes whose values have been cropped
     * @param cropLength            Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted
     *                              matching terms
     * @param filterArray           String array that can take multiple nested
     *                              filters
     * @param showMatchesPosition   Defines whether an object that contains
     *                              information about the showMatchesPosition should
     *                              be returned or not
     * @param facets                Facets for which to retrieve the matching count
     * @param sort                  Sort queries by an attribute value
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
            boolean showMatchesPosition,
            String[] facets,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                null,
                null,
                null,
                attributesToHighlight,
                null,
                filterArray,
                showMatchesPosition,
                facets,
                sort);
    }

    /**
     * Full SearchRequest Constructor for building search queries with 2D filter
     * Array
     *
     * @param q                     Query string
     * @param offset                Number of documents to skip
     * @param limit                 Maximum number of documents returned
     * @param attributesToRetrieve  Attributes to display in the returned documents
     * @param attributesToCrop      Attributes whose values have been cropped
     * @param cropLength            Length used to crop field values
     * @param cropMarker            String to customize default crop marker, default
     *                              value: …
     * @param highlightPreTag       String to customize highlight tag before every
     *                              highlighted query
     *                              terms, default value: <em>
     * @param highlightPostTag      String to customize highlight tag after every
     *                              highlighted query
     *                              terms, default value: </em>
     * @param attributesToHighlight Attributes whose values will contain highlighted
     *                              matching terms
     * @param filterArray           String array that can take multiple nested
     *                              filters
     * @param showMatchesPosition   Defines whether an object that contains
     *                              information about the showMatchesPosition should
     *                              be returned or not
     * @param facets                Facets for which to retrieve the matching count
     * @param sort                  Sort queries by an attribute value
     */
    public SearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[][] filterArray,
            boolean showMatchesPosition,
            String[] facets,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                cropMarker,
                highlightPreTag,
                highlightPostTag,
                attributesToHighlight,
                null,
                filterArray,
                showMatchesPosition,
                facets,
                sort);
    }

    private SearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            String[][] filterArray,
            boolean showMatchesPosition,
            String[] facets,
            String[] sort) {
        this.q = q;
        this.offset = offset;
        this.limit = limit;
        this.attributesToRetrieve = attributesToRetrieve;
        this.attributesToCrop = attributesToCrop;
        this.cropLength = cropLength;
        this.cropMarker = cropMarker;
        this.highlightPreTag = highlightPreTag;
        this.highlightPostTag = highlightPostTag;
        this.attributesToHighlight = attributesToHighlight;
        this.setFilter(filter);
        this.setFilterArray(filterArray);
        this.showMatchesPosition = showMatchesPosition;
        this.facets = facets;
        this.sort = sort;
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
     * Method to set the cropMarker
     *
     * @param cropMarker Marker used to crop field values
     * @return altered SearchRequest
     */
    public SearchRequest setCropMarker(String cropMarker) {
        this.cropMarker = cropMarker;
        return this;
    }

    /**
     * Method to set the highlightPreTag
     *
     * @param highlightPreTag Highlight tag use before every highlighted query terms
     * @return altered SearchRequest
     */
    public SearchRequest setHighlightPreTag(String highlightPreTag) {
        this.highlightPreTag = highlightPreTag;
        return this;
    }

    /**
     * Method to set the highlightPostTag
     *
     * @param highlightPostTag Highlight tag use after every highlighted query terms
     * @return altered SearchRequest
     */
    public SearchRequest setHighlightPostTag(String highlightPostTag) {
        this.highlightPostTag = highlightPostTag;
        return this;
    }

    /**
     * Method to set the attributesToHighlight
     *
     * @param attributesToHighlight Attributes whose values will contain highlighted
     *                              matching terms
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
    public SearchRequest setFilter(String[] filter) {
        if (filter != null) {
            this.filter = filter;
        }
        return this;
    }

    public SearchRequest setFilterArray(String[][] filterArray) {
        if (filterArray != null) {
            this.filterArray = filterArray;
        }
        return this;
    }

    /**
     * Method to set the showMatchesPosition boolean
     *
     * @param showMatchesPosition Defines whether an object that contains
     *                            information about the matches should
     *                            be returned or not
     * @return altered SearchRequest
     */
    public SearchRequest setShowMatchesPosition(boolean showMatchesPosition) {
        this.showMatchesPosition = showMatchesPosition;
        return this;
    }

    /**
     * Method to set the facets
     *
     * @param facets Facets for which to retrieve the matching count
     * @return altered SearchRequest
     */
    public SearchRequest setFacets(String[] facets) {
        this.facets = facets;
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

        JSONObject jsonObject = new JSONObject()
                .put("q", this.q)
                .put("offset", this.offset)
                .put("limit", this.limit)
                .put("attributesToRetrieve", this.attributesToRetrieve)
                .put("cropLength", this.cropLength)
                .put("cropMarker", this.cropMarker)
                .put("highlightPreTag", this.highlightPreTag)
                .put("highlightPostTag", this.highlightPostTag)
                .put("showMatchesPosition", this.showMatchesPosition)
                .put("facets", this.facets)
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
