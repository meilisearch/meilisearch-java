package com.meilisearch.sdk.model.batch.res;

import com.meilisearch.sdk.enums.OperationType;
import com.meilisearch.sdk.model.TaskStatus;
import java.util.Map;
import lombok.Data;

@Data
public class StatDetails {
    private int totalNbTasks;
    private Map<TaskStatus, Integer> status;
    private Map<OperationType, Integer> types;
    private Map<String, Integer> indexUids;
}
