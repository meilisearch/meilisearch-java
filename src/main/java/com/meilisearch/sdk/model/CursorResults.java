package com.meilisearch.sdk.model;

import java.util.List;
import lombok.Data;

/**
 * Data structure paginated response Currently used in : Batches.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/batches#response">API
 *     specification</a>
 */
@Data
public class CursorResults<T> {
    private List<T> results;
    private int limit;
    private int from;
    private Integer next;
    private int total;
}
