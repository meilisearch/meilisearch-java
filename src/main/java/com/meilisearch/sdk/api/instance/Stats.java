package com.meilisearch.sdk.api.instance;

import java.util.Date;
import java.util.Map;

public class Stats {
    private long databaseSize;
    private Date lastUpdate;
    private Map<String, IndexStats> indexes;

    public Stats() {}

    public Stats(long databaseSize, Date lastUpdate, Map<String, IndexStats> indexes) {
        this.databaseSize = databaseSize;
        this.lastUpdate = lastUpdate;
        this.indexes = indexes;
    }

    public long getDatabaseSize() {
        return databaseSize;
    }

    public void setDatabaseSize(long databaseSize) {
        this.databaseSize = databaseSize;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Map<String, IndexStats> getIndexes() {
        return indexes;
    }

    public void setIndexes(Map<String, IndexStats> indexes) {
        this.indexes = indexes;
    }
}
