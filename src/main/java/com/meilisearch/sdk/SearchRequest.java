package com.meilisearch.sdk;

import com.meilisearch.sdk.model.MatchingStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

/** Search request query string builder */
@Getter
@Setter
@Accessors(chain = true)
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
    private MatchingStrategy matchingStrategy;
    private String[] attributesToHighlight;
    private String[] filter;
    private String[][] filterArray;
    private boolean showMatchesPosition;
    private String[] facets;
    private String[] sort;

    /** Empty SearchRequest constructor */
    public SearchRequest() {}

    /**
     * Constructor for SearchRequest for building search queries with the default values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false, facets: null, sort:
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
     * null, filter: null, showMatchesPosition: false, facets: null, sort: null
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
     * null, filter: null, showMatchesPosition: false, facets: null, sort: null
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
     * attributesToCrop: null, cropLength: 200, attributesToHighlight: null, filter: null,
     * showMatchesPosition: false, facets: null, sort: null
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
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param showMatchesPosition Defines whether an object that contains information about the
     *     matches should be returned or not
     * @param facets Facets for which to retrieve the matching count
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
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param cropMarker String to customize default crop marker, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query terms
     * @param highlightPostTag String to customize highlight tag after every highlighted query terms
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param showMatchesPosition Defines whether an object that contains information about the
     *     matches should be returned or not
     * @param facets Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
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
                null,
                attributesToHighlight,
                filter,
                null,
                showMatchesPosition,
                facets,
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
     * @param showMatchesPosition Defines whether an object that contains information about the
     *     matches should be returned or not
     * @param facets Facets for which to retrieve the matching count
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
                null,
                attributesToHighlight,
                null,
                filterArray,
                showMatchesPosition,
                facets,
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
     * @param cropMarker String to customize default crop marker, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query terms
     * @param highlightPostTag String to customize highlight tag after every highlighted query terms
     * @param matchingStrategy MatchingStrategy to defines the strategy used to match query terms in
     *     documents
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filterArray String array that can take multiple nested filters
     * @param showMatchesPosition Defines whether an object that contains information about the
     *     matches should be returned or not
     * @param facets Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
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
            MatchingStrategy matchingStrategy,
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
                matchingStrategy,
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
            MatchingStrategy matchingStrategy,
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
        this.matchingStrategy = matchingStrategy;
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
     * <p>This method is an alias of setQ()
     *
     * @param q Query String
     * @return SearchRequest
     */
    public SearchRequest setQuery(String q) {
        return setQ(q);
    }

    /**
     * Method that returns the JSON String of the SearchRequest
     *
     * @return JSON String of the SearchRequest query
     */
    @Override
    public String toString() {
        JSONObject jsonObject =
                new JSONObject()
                        .put("q", this.q)
                        .put("offset", this.offset)
                        .put("limit", this.limit)
                        .put("attributesToRetrieve", this.attributesToRetrieve)
                        .put("cropLength", this.cropLength)
                        .put("cropMarker", this.cropMarker)
                        .put("highlightPreTag", this.highlightPreTag)
                        .put("highlightPostTag", this.highlightPostTag)
                        .put(
                                "matchingStrategy",
                                this.matchingStrategy == null
                                        ? null
                                        : this.matchingStrategy.toString())
                        .put("showMatchesPosition", this.showMatchesPosition)
                        .put("facets", this.facets)
                        .put("sort", this.sort)
                        .putOpt("attributesToCrop", this.attributesToCrop)
                        .putOpt("attributesToHighlight", this.attributesToHighlight)
                        .putOpt("filter", this.filter)
                        .putOpt("filter", this.filterArray);

        return jsonObject.toString();
    }
}
