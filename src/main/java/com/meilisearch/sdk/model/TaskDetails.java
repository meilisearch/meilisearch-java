package com.meilisearch.sdk.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import lombok.Getter;

/** Task details data structure */
@Getter
public class TaskDetails {
    protected int receivedDocuments;
    protected int indexedDocuments;
    protected int deletedDocuments;
    protected String primaryKey;
    protected String[] rankingRules;
    protected String[] searchableAttributes;
    protected String[] displayedAttributes;
    @JsonDeserialize(using = com.meilisearch.sdk.json.TaskDetailsFilterableAttributesDeserializer.class)
    protected String[] filterableAttributes;
    protected String[] sortableAttributes;
    protected String[] stopWords;
    protected Map<String, String[]> synonyms;
    protected String distinctAttribute;
    protected TypoTolerance typoTolerance;
    protected int providedIds;
    protected Pagination pagination;
    protected Faceting faceting;
    protected String dumpUid;
    protected int matchedTasks;
    protected int canceledTasks;
    protected String originalFilter;
    protected int deletedTasks;
    protected SwapIndexesParams[] swaps;

    public TaskDetails() {}
}
