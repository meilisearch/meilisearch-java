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

    /**
     * Method that returns the JSON String of the SearchRequest
     *
     * @return JSON String of the SearchRequest query
     */
 @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("q", this.getQ());
        jsonObject.put("offset", this.getOffset());
        jsonObject.put("limit", this.getLimit());
        jsonObject.put("attributesToRetrieve", getStrArrays(this.getAttributesToRetrieve()));
        jsonObject.put("cropLength", this.getCropLength());
        jsonObject.put("cropMarker", this.getCropMarker());
        jsonObject.put("highlightPreTag", this.getHighlightPreTag());
        jsonObject.put("highlightPostTag", this.getHighlightPostTag());
        jsonObject.put("matchingStrategy", this.getMatchingStrategy() == null ? null : this.getMatchingStrategy().toString());
        jsonObject.put("showMatchesPosition", this.getShowMatchesPosition());
        jsonObject.put("facets", getStrArrays(this.getFacets()));
        jsonObject.put("sort", getStrArrays(this.getSort()));
        jsonObject.put("page", this.getPage());
        jsonObject.put("hitsPerPage", this.getHitsPerPage());
        jsonObject.putOpt("attributesToCrop", getStrArrays(this.getAttributesToCrop()));
        jsonObject.putOpt("attributesToHighlight", getStrArrays(this.getAttributesToHighlight()));
        jsonObject.putOpt("filter", getStrArrays(this.getFilter()));
        jsonObject.putOpt("filter", this.getFilterArray());
        return jsonObject.toString();
    }

    /**
     * trans it to json String value 
     *
     * @param arr arr
     * @return {@code Object}
     */
    private static Object getStrArrays(String[] arr) {
        if (arr == null) {
            return null;
        }
        if (arr.length >= 1) {
            final JSONArray jsonArray = new JSONArray();
            for (String s : arr) {
                jsonArray.put(s);
            }
            return jsonArray;
        } else {
            return arr;
        }
    }
}
