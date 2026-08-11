package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Stats data structure of a Meilisearch Index
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats#get-stats-of-an-index">API
 *     specification</a>
 */
@Getter
@Setter
public class IndexStats {
    protected long numberOfDocuments;
    protected boolean isIndexing;
    protected Map<String, Integer> fieldDistribution;
    protected long rawDocumentDbSize;
    protected long avgDocumentSize;
    protected long numberOfEmbeddedDocuments;
    protected long numberOfEmbeddings;
    protected long indexSize;
    protected long usedIndexSize;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments,
            boolean isIndexing,
            Map<String, Integer> fieldDistribution,
            long rawDocumentDbSize,
            long avgDocumentSize,
            long numberOfEmbeddedDocuments,
            long numberOfEmbeddings) {
        this(
                numberOfDocuments,
                isIndexing,
                fieldDistribution,
                rawDocumentDbSize,
                avgDocumentSize,
                numberOfEmbeddedDocuments,
                numberOfEmbeddings,
                0L,
                0L);
    }

    public IndexStats(
            long numberOfDocuments,
            boolean isIndexing,
            Map<String, Integer> fieldDistribution,
            long rawDocumentDbSize,
            long avgDocumentSize,
            long numberOfEmbeddedDocuments,
            long numberOfEmbeddings,
            long indexSize,
            long usedIndexSize) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
        this.rawDocumentDbSize = rawDocumentDbSize;
        this.avgDocumentSize = avgDocumentSize;
        this.numberOfEmbeddedDocuments = numberOfEmbeddedDocuments;
        this.numberOfEmbeddings = numberOfEmbeddings;
        this.indexSize = indexSize;
        this.usedIndexSize = usedIndexSize;
    }

    @JsonProperty("isIndexing")
    public boolean isIndexing() {
        return isIndexing;
    }
}
