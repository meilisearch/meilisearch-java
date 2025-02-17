package com.meilisearch.sdk.model.batch.res;

import java.util.List;
import lombok.Data;

@Data
class BatchProgress {
    private List<StepDetails> steps;
    private int percentage;
}
