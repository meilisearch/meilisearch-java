package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;

@Getter
public class FacetsByIndexInfo {
    private HashMap<String, HashMap<String, Integer>> distribution;
    private HashMap<String, FacetRating> stats;
}
