package com.meilisearch.sdk.model;

import java.util.Date;
import java.util.Map;
import lombok.Getter;

/**
 * Meilisearch stats data structure when database sizes may be raw bytes or human-readable strings.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/stats">API specification</a>
 */
@Getter
public class StatsWithSizeFormat {
    protected Object databaseSize;
    protected Date lastUpdate;
    protected Map<String, IndexStatsWithSizeFormat> indexes;
    protected Object usedDatabaseSize;

    public StatsWithSizeFormat(
            Object databaseSize,
            Date lastUpdate,
            Map<String, IndexStatsWithSizeFormat> indexes,
            Object usedDatabaseSize) {
        this.databaseSize = databaseSize;
        this.lastUpdate = lastUpdate;
        this.indexes = indexes;
        this.usedDatabaseSize = usedDatabaseSize;
    }

    public StatsWithSizeFormat() {}
}
