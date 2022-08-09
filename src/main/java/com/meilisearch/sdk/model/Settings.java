package com.meilisearch.sdk.model;

import com.meilisearch.sdk.Index;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Data Structure for the Settings in an {@link Index}
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
 */
@Getter
@Setter
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

    public Settings() {}

    /**
     * Method that returns the JSON String of the update settings query
     *
     * @return JSON String of the update settings query
     */
    public String getUpdateQuery() {
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
        if (this.getTypoTolerance() != null) {
            jsonObject.put("typoTolerance", this.getTypoTolerance().toJson());
        }
        return jsonObject.toString();
    }
}
