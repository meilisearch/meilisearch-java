package com.meilisearch.sdk.model;

import com.meilisearch.sdk.model.batch_dto.Batch;
import lombok.Data;

@Data
public class BatchResults {
    private CursorResults<Batch> batchCursorResults;
}
