package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.TypoTolerance;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Structure for the Settings in an {@link Index}
 *
 * <p>Refer https://docs.meilisearch.com/references/settings.html
 */
public class Settings {
    @Getter @Setter private HashMap<String, String[]> synonyms;
    @Getter @Setter private String[] stopWords;
    @Getter @Setter private String[] rankingRules;
    @Getter @Setter private String[] filterableAttributes;
    @Getter @Setter private String distinctAttribute;
    @Getter @Setter private String[] searchableAttributes;
    @Getter @Setter private String[] displayedAttributes;
    @Getter @Setter private String[] sortableAttributes;
    @Getter @Setter private TypoTolerance typoTolerance;

    /** Empty SettingsRequest constructor */
    public Settings() {}
}
