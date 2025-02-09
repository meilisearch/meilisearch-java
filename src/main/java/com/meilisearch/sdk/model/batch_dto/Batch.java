package com.meilisearch.sdk.model.batch_dto;

import java.util.List;
import lombok.Data;

@Data
public class Batch {
    private int uid;
    private BatchDetails details;
    private BatchProgress progress;
    private StatDetails stats;
    private String startedAt;
    private String finishedAt;
    private String duration;
}

@Data
class BatchDetails {
    private Integer receivedDocuments;
    private Integer indexedDocuments;
    private Integer deletedDocuments;
}

@Data
class BatchProgress {
    private List<StepDetails> steps;
    private int percentage;
}

@Data
class StepDetails {
    private String currentStep;
    private int finished;
    private int total;
}
