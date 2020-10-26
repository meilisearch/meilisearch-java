package com.meilisearch.sdk.api.index;

import java.util.*;

public class Settings {
    private List<String> attributesForFaceting = new ArrayList<>();
    private String distinctAttribute;
    private List<String> searchableAttributes = new ArrayList<>();
    private List<String> displayedAttributes = new ArrayList<>();
    private List<String> rankingRules = new ArrayList<>();
    private List<String> stopWords = new ArrayList<>();
    private Map<String, List<String>> synonyms = new HashMap<>();

    public List<String> getAttributesForFaceting() {
        return attributesForFaceting;
    }

    public Settings setAttributesForFaceting(List<String> attributesForFaceting) {
        this.attributesForFaceting = attributesForFaceting;
        return this;
    }

    public String getDistinctAttribute() {
        return distinctAttribute;
    }

    public Settings setDistinctAttribute(String distinctAttribute) {
        this.distinctAttribute = distinctAttribute;
        return this;
    }

    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    public Settings setSearchableAttributes(List<String> searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
        return this;
    }

    public List<String> getDisplayedAttributes() {
        return displayedAttributes;
    }

    public Settings setDisplayedAttributes(List<String> displayedAttributes) {
        this.displayedAttributes = displayedAttributes;
        return this;
    }

    public List<String> getRankingRules() {
        return rankingRules;
    }

    public Settings setRankingRules(List<String> rankingRules) {
        this.rankingRules = rankingRules;
        return this;
    }

    public List<String> getStopWords() {
        return stopWords;
    }

    public Settings setStopWords(List<String> stopWords) {
        this.stopWords = stopWords;
        return this;
    }

    public Map<String, List<String>> getSynonyms() {
        return synonyms;
    }

    public Settings setSynonyms(Map<String, List<String>> synonyms) {
        this.synonyms = synonyms;
        return this;
    }

    public Settings addSynonym(String word, String... synonyms) {
        this.synonyms.computeIfAbsent(word, s -> new ArrayList<>(synonyms.length)).addAll(Arrays.asList(synonyms));
        return this;
    }
}
