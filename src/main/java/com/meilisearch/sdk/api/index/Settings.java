package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.Index;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Structure for the Settings in an {@link Index}
 *
 * <p>Refer https://docs.meilisearch.com/references/settings.html
 */
@Getter
@Setter
public class Settings {
    private HashMap<String, String[]> synonyms;
    private String[] stopWords;
    private String[] rankingRules;
    private String[] filterableAttributes;
    private String distinctAttribute;
    private String[] searchableAttributes;
    private String[] displayedAttributes;
    private String[] sortableAttributes;

    /** Empty SettingsRequest constructor */
    public Settings() {}
}
