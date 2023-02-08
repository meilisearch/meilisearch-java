package com.meilisearch.sdk.model;

public enum MatchingStrategy {
    ALL("all"),
    LAST("last");

    public final String matchingStrategy;

    private MatchingStrategy(String matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    @Override
    public String toString() {
        return this.matchingStrategy;
    }
}
