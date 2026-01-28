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

    /**
     * Best-effort, flattened view of filterable attributes as simple names.
     *
     * <p>When tasks return granular filterable attribute objects, all {@code attributePatterns} from
     * those objects are flattened into this array. Entries that are null, missing, or non-textual
     * are skipped. Granular feature flags are not exposed here; use settings APIs with
     * {@link com.meilisearch.sdk.model.FilterableAttributesConfig} for structured access.
     */
    @JsonDeserialize(
            using = com.meilisearch.sdk.json.TaskDetailsFilterableAttributesDeserializer.class)
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
