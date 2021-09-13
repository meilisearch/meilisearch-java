package com.meilisearch.sdk;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Data Structure for the Settings in an {@link Index}
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
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

    /** Empty SettingsRequest constructor */
    public Settings() {}

    /**
     * Method that returns the JSON String of the update settings query
     *
     * @return JSON String of the update settings query
     */
    String getUpdateQuery() {
        JSONObject jsonObject = new JSONObject();
        if (this.getSynonyms() != null) {
            jsonObject.put("synonyms", this.getSynonyms());
        }
        if (this.getStopWords() != null) {
            jsonObject.put("stopWords", this.getStopWords());
        }
        if (this.getRankingRules() != null) {
            jsonObject.put("rankingRules", this.getRankingRules());
        }
        if (this.getFilterableAttributes() != null) {
            jsonObject.put("filterableAttributes", this.getFilterableAttributes());
        }
        if (this.getDistinctAttribute() != null) {
            jsonObject.put("distinctAttribute", this.getDistinctAttribute());
        }
        if (this.getSearchableAttributes() != null) {
            jsonObject.put("searchableAttributes", this.getSearchableAttributes());
        }
        if (this.getDisplayedAttributes() != null) {
            jsonObject.put("displayedAttributes", this.getDisplayedAttributes());
        }
        if (this.getSortableAttributes() != null) {
            jsonObject.put("sortableAttributes", this.getSortableAttributes());
        }
        return jsonObject.toString();
    }
}
