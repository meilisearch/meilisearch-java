package com.meilisearch.sdk.model;

import java.util.Date;
import java.util.Map;
import lombok.Getter;

/**
 * Meilisearch stats data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
 */
@Getter
public class Stats {
    protected long databaseSize;
    protected Date lastUpdate;
    protected Map<String, IndexStats> indexes;

    public Stats(long databaseSize, Date lastUpdate, Map<String, IndexStats> indexes) {
        this.databaseSize = databaseSize;
        this.lastUpdate = lastUpdate;
        this.indexes = indexes;
    }

    public Stats() {}
}
