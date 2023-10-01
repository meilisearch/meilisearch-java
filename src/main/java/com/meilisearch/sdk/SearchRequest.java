package com.meilisearch.sdk;

import com.meilisearch.sdk.model.MatchingStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

/** Search request query string builder */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class SearchRequest {
    private String q;
    private Integer offset;
    private Integer limit;
    private String[] attributesToRetrieve;
    private String[] attributesToCrop;
    private Integer cropLength;
    private String cropMarker;
    private String highlightPreTag;
    private String highlightPostTag;
    private MatchingStrategy matchingStrategy;
    private String[] attributesToHighlight;
    private String[] filter;
    private String[][] filterArray;
    private Boolean showMatchesPosition;
    private String[] facets;
    private String[] sort;
    protected Integer page;
    protected Integer hitsPerPage;

    /**
     * Constructor for SearchRequest for building search queries with the default values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, showMatchesPosition: false, facets: null, sort:
     * null
     *
     * @param q Query String
     */
    public SearchRequest(String q) {
        this();
        this.q = q;
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

    public SearchRequest setAttributesToRetrieve(String... attributesToRetrieve) {
        this.attributesToRetrieve = attributesToRetrieve;
        return this;
    }

    public SearchRequest setAttributesToCrop(String... attributesToCrop) {
        this.attributesToCrop = attributesToCrop;
        return this;
    }

    public SearchRequest setAttributesToHighlight(String... attributesToHighlight) {
        this.attributesToHighlight = attributesToHighlight;
        return this;
    }

    public SearchRequest setFilter(String... filter) {
        this.filter = filter;
        return this;
    }

    public SearchRequest setFacets(String... facets) {
        this.facets = facets;
        return this;
    }

    public SearchRequest setSort(String... sort) {
        this.sort = sort;
        return this;
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
                        .put("page", this.page)
                        .put("hitsPerPage", this.hitsPerPage)
                        .putOpt("attributesToCrop", this.attributesToCrop)
                        .putOpt("attributesToHighlight", this.attributesToHighlight)
                        .putOpt("filter", this.filter)
                        .putOpt("filter", this.filterArray);

        return jsonObject.toString();
    }

    /** The SearchRequestBuilder class is used to override default lombok setter builder methods */
    public static class SearchRequestBuilder {

        public SearchRequestBuilder attributesToRetrieve(String... attributesToRetrieve) {
            this.attributesToRetrieve = attributesToRetrieve;
            return this;
        }

        public SearchRequestBuilder attributesToCrop(String... attributesToCrop) {
            this.attributesToCrop = attributesToCrop;
            return this;
        }

        public SearchRequestBuilder attributesToHighlight(String... attributesToHighlight) {
            this.attributesToHighlight = attributesToHighlight;
            return this;
        }

        public SearchRequestBuilder filter(String... filter) {
            this.filter = filter;
            return this;
        }

        public SearchRequestBuilder facets(String... facets) {
            this.facets = facets;
            return this;
        }

        public SearchRequestBuilder sort(String... sort) {
            this.sort = sort;
            return this;
        }
    }
}
