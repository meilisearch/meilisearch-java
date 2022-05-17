package com.meilisearch.sdk;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    protected TypoTolerance typoTolerance;
}
