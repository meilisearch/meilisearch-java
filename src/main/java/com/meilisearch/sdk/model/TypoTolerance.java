package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Typo Tolerance setting data structure
 *
 * <p>https://www.meilisearch.com/docs/reference/api/settings#typo-tolerance-object
 */
@Getter
@Setter
@Accessors(chain = true)
public class TypoTolerance {
    protected boolean enabled = true;
    protected HashMap<String, Integer> minWordSizeForTypos;
    protected String[] disableOnWords;
    protected String[] disableOnAttributes;

    public void setDisableOnWords(String... disableOnWords) {
        this.disableOnWords = disableOnWords;
    }

    public void setDisableOnAttributes(String... disableOnAttributes) {
        this.disableOnAttributes = disableOnAttributes;
    }

    public TypoTolerance() {}
}
