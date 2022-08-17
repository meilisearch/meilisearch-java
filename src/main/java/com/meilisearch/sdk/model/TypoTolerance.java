package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * Data structure for the Typo tolerance setting
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/typo_tolerance.html
 */
@Getter
@Setter
public class TypoTolerance {
    protected boolean enabled = true;
    protected HashMap<String, Integer> minWordSizeForTypos;
    protected String[] disableOnWords;
    protected String[] disableOnAttributes;

    public TypoTolerance() {}
}
