package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum MatchingStrategy {
    @SerializedName("all")
    ALL("all"),
    @SerializedName("last")
    LAST("last"),
    @SerializedName("frequency")
    FREQUENCY("frequency");

    public final String matchingStrategy;

    private MatchingStrategy(String matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.matchingStrategy;
    }
}
