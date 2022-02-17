package com.meilisearch.sdk;

import java.util.Map;

public class Details {

    public Details() {}

    protected int receivedDocuments;
    protected int indexedDocuments;
    protected int deletedDocuments;
    protected String primaryKey;
    protected String[] rankingRules;
    protected String[] searchableAttributes;
    protected String[] displayedAttributes;
    protected String[] filterableAttributes;
    protected String[] sortableAttributes;
    protected String[] stopWords;
    protected Map<String, String[]> synonyms;
    protected String distinctAttribute;

    public int getReceivedDocuments() {
        return receivedDocuments;
    }

    public void setReceivedDocuments(int receivedDocuments) {
        this.receivedDocuments = receivedDocuments;
    }

    public int getIndexedDocuments() {
        return indexedDocuments;
    }

    public void setIndexedDocuments(int indexedDocuments) {
        this.indexedDocuments = indexedDocuments;
    }

    public int getDeletedDocuments() {
        return deletedDocuments;
    }

    public void setDeletedDocuments(int deletedDocuments) {
        this.deletedDocuments = deletedDocuments;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String[] getRankingRules() {
        return rankingRules;
    }

    public void setRankingRules(String[] rankingRules) {
        this.rankingRules = rankingRules;
    }

    public String[] getSearchableAttributes() {
        return searchableAttributes;
    }

    public void setSearchableAttributes(String[] searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
    }

    public String[] getDisplayedAttributes() {
        return displayedAttributes;
    }

    public void setDisplayedAttributes(String[] displayedAttributes) {
        this.displayedAttributes = displayedAttributes;
    }

    public String[] getFilterableAttributes() {
        return filterableAttributes;
    }

    public void setFilterableAttributes(String[] filterableAttributes) {
        this.filterableAttributes = filterableAttributes;
    }

    public String[] getSortableAttributes() {
        return sortableAttributes;
    }

    public void setSortableAttributes(String[] sortableAttributes) {
        this.sortableAttributes = sortableAttributes;
    }

    public String[] getStopWords() {
        return stopWords;
    }

    public void setStopWords(String[] stopWords) {
        this.stopWords = stopWords;
    }

    public Map<String, String[]> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Map<String, String[]> synonyms) {
        this.synonyms = synonyms;
    }

    public String getDistinctAttribute() {
        return distinctAttribute;
    }

    public void setDistinctAttribute(String distinctAttribute) {
        this.distinctAttribute = distinctAttribute;
    }
}
