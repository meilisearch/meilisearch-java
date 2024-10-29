package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

/**
 * LocalizedAttribute setting data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#localized-attributes">API
 *     specification</a>
 */
@Getter
@Setter
public class LocalizedAttribute {
    protected String[] attributePatterns;
    protected String[] locales;

    public LocalizedAttribute() {}
}
