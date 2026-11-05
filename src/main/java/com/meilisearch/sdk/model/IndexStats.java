package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;

/**
 * Stats data structure of a Meilisearch Index
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats#get-stats-of-an-index">API
 *     specification</a>
 */
@Getter
public class IndexStats {
    protected long numberOfDocuments;

    @JsonProperty("isIndexing")
    protected boolean isIndexing;

    protected Map<String, Integer> fieldDistribution;
    protected long rawDocumentDbSize;
    protected long avgDocumentSize;
    protected long numberOfEmbeddedDocuments;
    protected long numberOfEmbeddings;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments,
            boolean isIndexing,
            Map<String, Integer> fieldDistribution,
            long rawDocumentDbSize,
            long avgDocumentSize,
            long numberOfEmbeddedDocuments,
            long numberOfEmbeddings) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
        this.rawDocumentDbSize = rawDocumentDbSize;
        this.avgDocumentSize = avgDocumentSize;
        this.numberOfEmbeddedDocuments = numberOfEmbeddedDocuments;
        this.numberOfEmbeddings = numberOfEmbeddings;
    }

    /** Named explicitly because Jackson would otherwise call this property {@code indexing}. */
    @JsonProperty("isIndexing")
    public boolean isIndexing() {
        return isIndexing;
    }
}
