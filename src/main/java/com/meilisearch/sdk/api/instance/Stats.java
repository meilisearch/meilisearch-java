package com.meilisearch.sdk.api.instance;

import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Stats {
    @Getter @Setter  private long databaseSize;
    @Getter @Setter  private Date lastUpdate;
    @Getter @Setter  private Map<String, IndexStats> indexes;

    public Stats() {}

    public Stats(long databaseSize, Date lastUpdate, Map<String, IndexStats> indexes) {
        this.databaseSize = databaseSize;
        this.lastUpdate = lastUpdate;
        this.indexes = indexes;
    }
    
}
