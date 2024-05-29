package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Faceting {
    protected int maxValuesPerFacet;
    protected HashMap<String, FacetSortValue> sortFacetValuesBy;

    public Faceting() {}
}
