package com.meilisearch.sdk;

import org.json.JSONObject;

public class FederationOptions {

    private Double weight;

    public FederationOptions setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Method that returns the JSON String of the FederationOptions
     *
     * @return JSON String of the FederationOptions
     */
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject().put("weight", this.weight);
        return jsonObject.toString();
    }
}
