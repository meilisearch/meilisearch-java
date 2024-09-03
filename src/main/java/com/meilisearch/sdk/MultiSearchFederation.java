package com.meilisearch.sdk;

import org.json.JSONObject;

public class MultiSearchFederation {
    
    private Integer limit;
    private Integer offset;

    public MultiSearchFederation setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public MultiSearchFederation setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }
    
    public Integer getLimit(){
        return this.limit;
    }

    public Integer getOffset(){
        return this.offset;
    }

    /**
     * Method that returns the JSON String of the MultiSearchFederation
     *
     * @return JSON String of the MultiSearchFederation 
     */
    @Override
    public String toString(){
        JSONObject jsonObject =
                new JSONObject()
                .put("limit", this.limit)
                .put("offset", this.offset);
        return jsonObject.toString();
    }
}
