package com.meilisearch.sdk;

import java.util.Map;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public class MultiSearchFederation {

    private Integer limit;
    private Integer offset;
    private MergeFacets mergeFacets;
    private Map<String, String[]> facetsByIndex;

    public MultiSearchFederation setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public MultiSearchFederation setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public MultiSearchFederation setMergeFacets(MergeFacets mergeFacets) {
        this.mergeFacets = mergeFacets;
        return this;
    }

    public MultiSearchFederation setFacetsByIndex(Map<String, String[]> facetsByIndex) {
        this.facetsByIndex = facetsByIndex;
        return this;
    }

    /**
     * Method that returns the JSON String of the MultiSearchFederation
     *
     * @return JSON String of the MultiSearchFederation
     */
    @Override
    public String toString() {
        JSONObject jsonObject =
                new JSONObject().put("limit", this.limit).put("offset", this.offset);
        return jsonObject.toString();
    }
}
