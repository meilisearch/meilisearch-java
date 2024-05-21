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

/** Facet search request query string builder */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class FacetSearchRequest {
    private String facetName;
    private String facetQuery;
    private String q;
    private MatchingStrategy matchingStrategy;
    private String[] attributesToSearchOn;
    private String[] filter;
    private String[][] filterArray;

    /**
     * Constructor for FacetSearchRequest for building facet search queries with the default values:
     * facetQuery: null, query: null, matchingStrategy: null, attributesToSearchOn: null, filter:
     * null
     *
     * @param facetName FacetName String
     */
    public FacetSearchRequest(String facetName) {
        this();
        this.facetName = facetName;
    }

    /**
     * Method to set the Query String
     *
     * <p>This method is an alias of setQ()
     *
     * @param q Query String
     * @return SearchRequest
     */
    public FacetSearchRequest setQuery(String q) {
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
                        .put("facetName", this.facetName)
                        .put("facetQuery", this.facetQuery)
                        .put("q", this.q)
                        .put(
                                "matchingStrategy",
                                this.matchingStrategy == null
                                        ? null
                                        : this.matchingStrategy.toString())
                        .putOpt("attributesToSearchOn", this.attributesToSearchOn)
                        .putOpt("filter", this.filter)
                        .putOpt("filter", this.filterArray);

        return jsonObject.toString();
    }
}
