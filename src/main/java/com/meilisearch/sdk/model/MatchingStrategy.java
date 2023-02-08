package com.meilisearch.sdk.model;

public enum MatchingStrategy {
    ALL("all"),
    LAST("Last");

    public final String matchingStrategy;

    private MatchingStrategy(String matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public String toString() {
        return this.matchingStrategy;
    }
}
