package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Data structure of Meilisearch response for the Typo Tolerance Setting
 *
 * <p>https://docs.meilisearch.com/reference/api/typo_tolerance.html
 */
@Getter
@Setter
@Accessors(chain = true)
public class TypoTolerance {
    protected boolean enabled = true;
    protected HashMap<String, Integer> minWordSizeForTypos;
    protected String[] disableOnWords;
    protected String[] disableOnAttributes;

    public TypoTolerance() {}
}
