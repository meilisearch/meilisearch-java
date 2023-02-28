package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Faceting {
    protected int maxValuesPerFacet;

    public Faceting() {}
}
