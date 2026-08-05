package com.meilisearch.sdk.model;

import java.util.Map;
import lombok.Getter;

/**
 * Stats data structure of a Meilisearch Index when database sizes may be raw bytes or human-readable
 * strings.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/indexes/get-stats-of-index">API
 *     specification</a>
 */
@Getter
public class IndexStatsWithSizeFormat {
    protected long numberOfDocuments;
    protected boolean isIndexing;
    protected Map<String, Integer> fieldDistribution;
    protected Object rawDocumentDbSize;
    protected Object avgDocumentSize;
    protected Object maxDocumentSize;
    protected Long numberOfEmbeddedDocuments;
    protected Long numberOfEmbeddings;
    protected Map<String, Object> internalDatabaseSizes;

    public IndexStatsWithSizeFormat() {}

    public IndexStatsWithSizeFormat(
            long numberOfDocuments,
            boolean isIndexing,
            Map<String, Integer> fieldDistribution,
            Object rawDocumentDbSize,
            Object avgDocumentSize,
            Object maxDocumentSize,
            Long numberOfEmbeddedDocuments,
            Long numberOfEmbeddings,
            Map<String, Object> internalDatabaseSizes) {
        this.numberOfDocuments = numberOfDocuments;
        this.isIndexing = isIndexing;
        this.fieldDistribution = fieldDistribution;
        this.rawDocumentDbSize = rawDocumentDbSize;
        this.avgDocumentSize = avgDocumentSize;
        this.maxDocumentSize = maxDocumentSize;
        this.numberOfEmbeddedDocuments = numberOfEmbeddedDocuments;
        this.numberOfEmbeddings = numberOfEmbeddings;
        this.internalDatabaseSizes = internalDatabaseSizes;
    }
}
