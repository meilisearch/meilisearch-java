package com.meilisearch.sdk.model.batch_dto;

import java.util.Map;
import lombok.Data;

@Data
public class StatDetails {
    private int totalNbTasks;
    private Map<String, Integer> status;
    private Map<String, Integer> types;
    private Map<String, Integer> indexUids;
}
