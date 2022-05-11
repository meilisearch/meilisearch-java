package com.meilisearch.sdk.api.instance;

import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
