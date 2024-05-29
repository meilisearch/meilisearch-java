package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FacetRating {
    protected double min;
    protected double max;

    public FacetRating() {}
}
