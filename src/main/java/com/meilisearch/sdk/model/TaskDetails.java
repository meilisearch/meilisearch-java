package com.meilisearch.sdk.model;

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

    public void setRankingRules(String... rankingRules) {
        this.rankingRules = rankingRules;
    }

    public void setSearchableAttributes(String... searchableAttributes) {
        this.searchableAttributes = searchableAttributes;
    }

    public void setDisplayedAttributes(String... displayedAttributes) {
        this.displayedAttributes = displayedAttributes;
    }

    public void setFilterableAttributes(String... filterableAttributes) {
        this.filterableAttributes = filterableAttributes;
    }

    public void setSortableAttributes(String... sortableAttributes) {
        this.sortableAttributes = sortableAttributes;
    }

    public void setStopWords(String... stopWords) {
        this.stopWords = stopWords;
    }

    public void setSwaps(SwapIndexesParams... swaps) {
        this.swaps = swaps;
    }

    public TaskDetails() {}
}
