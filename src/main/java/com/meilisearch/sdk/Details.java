package com.meilisearch.sdk;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Details {

    public Details() {}

    @Getter @Setter protected int receivedDocuments;
    @Getter @Setter protected int indexedDocuments;
    @Getter @Setter protected int deletedDocuments;
    @Getter @Setter protected String primaryKey;
    @Getter @Setter protected String[] rankingRules;
    @Getter @Setter protected String[] searchableAttributes;
    @Getter @Setter protected String[] displayedAttributes;
    @Getter @Setter protected String[] filterableAttributes;
    @Getter @Setter protected String[] sortableAttributes;
    @Getter @Setter protected String[] stopWords;
    @Getter @Setter protected Map<String, String[]> synonyms;
    @Getter @Setter protected String distinctAttribute;
}
