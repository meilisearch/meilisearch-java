package com.meilisearch.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LocalizedAttribute setting data structure
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#localized-attributes">API
 *     specification</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalizedAttribute {
    protected String[] attributePatterns;
    protected String[] locales;
}
