package com.meilisearch.sdk.model.batch.res;

import lombok.Data;

@Data
class StepDetails {
    private String currentStep;
    private int finished;
    private int total;
}
