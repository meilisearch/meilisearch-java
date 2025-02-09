package com.meilisearch.sdk.model;

import java.util.List;
import lombok.Data;

@Data
public class CursorResults<T> {
    private List<T> results;
    private int limit;
    private int from;
    private Integer next;
    private int total;
}
