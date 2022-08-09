package com.meilisearch.sdk.model;

import java.util.Date;
import java.util.Map;
import lombok.Getter;

/**
 * Data structure for the stats
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/stats.html
 */
@Getter
public class Stats {
    protected long databaseSize;
    protected Date lastUpdate;
    protected Map<String, IndexStats> indexes;

    public Stats() {}

    public Stats(long databaseSize, Date lastUpdate, Map<String, IndexStats> indexes) {
        this.databaseSize = databaseSize;
        this.lastUpdate = lastUpdate;
        this.indexes = indexes;
    }
}
