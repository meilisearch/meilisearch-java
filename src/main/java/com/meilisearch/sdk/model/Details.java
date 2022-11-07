package com.meilisearch.sdk.model;

import java.util.Map;
import lombok.Getter;

@Getter
public class Details {
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
    protected TypoTolerance typoTolerance;

    public Details() {}
}
