package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;

@Getter
public class FacetsByIndexInfo {
    private Object distribution;
    private HashMap<String, FacetRating> stats;
}
