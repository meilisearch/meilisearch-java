package com.meilisearch.sdk.model;

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
    protected boolean isIndexing;
    protected Map<String, Integer> fieldDistribution;
    protected long rawDocumentDbSize;
    protected long avgDocumentSize;
    protected long numberOfEmbeddedDocuments;
    protected long numberOfEmbeddings;

    public IndexStats() {}

    public IndexStats(
            long numberOfDocuments, boolean isIndexing, Map<String, Integer> fieldDistribution,
            long rawDocumentDbSize, long avgDocumentSize, long numberOfEmbeddedDocuments, long numberOfEmbeddings) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
        this.rawDocumentDbSize = rawDocumentDbSize;
        this.avgDocumentSize = avgDocumentSize;
        this.numberOfEmbeddedDocuments = numberOfEmbeddedDocuments;
        this.numberOfEmbeddings = numberOfEmbeddings;
    }
}
