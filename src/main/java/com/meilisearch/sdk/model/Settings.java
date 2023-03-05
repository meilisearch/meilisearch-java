package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Meilisearch settings data structure
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
 */
@Getter
@Setter
@Accessors(chain = true)
public class Settings {

    protected HashMap<String, String[]> synonyms;
    protected String[] stopWords;
    protected String[] rankingRules;
    protected String[] filterableAttributes;
    protected String distinctAttribute;
    protected String[] searchableAttributes;
    protected String[] displayedAttributes;
    protected String[] sortableAttributes;
    protected TypoTolerance typoTolerance;
    public Pagination pagination;

    public Settings() {}
}
