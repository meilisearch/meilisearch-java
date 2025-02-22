package com.meilisearch.sdk.model.batch.res;

import com.meilisearch.sdk.model.TaskDetails;
import lombok.Data;

/**
 * Data structure of the batch object response
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/batches#batch-object">API
 *     specification</a>
 */
@Data
public class Batch {
    private int uid = 0;
    private TaskDetails details;
    private BatchProgress progress;
    private StatDetails stats;
    private String startedAt;
    private String finishedAt;
    private String duration;
}
