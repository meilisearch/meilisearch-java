package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Typo Tolerance setting data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#typo-tolerance-object">API
 *     specification</a>
 */
@Getter
@Setter
@Accessors(chain = true)
public class TypoTolerance {
    protected boolean enabled = true;
    protected HashMap<String, Integer> minWordSizeForTypos;
    protected String[] disableOnWords;
    protected String[] disableOnAttributes;
    protected Boolean disableOnNumbers;

    public TypoTolerance() {}
}
