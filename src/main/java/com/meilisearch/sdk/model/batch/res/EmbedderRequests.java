package com.meilisearch.sdk.model.batch.res;

import lombok.Data;

@Data
public class EmbedderRequests {
    private int total;
    private int failed;
    private String lastError;
}
