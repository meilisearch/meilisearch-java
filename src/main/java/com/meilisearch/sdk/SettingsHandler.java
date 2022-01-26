package com.meilisearch.sdk;

import com.google.gson.Gson;
import java.util.Map;

/**
 * Settings Handler for manipulation of an Index {@link Settings}
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
 */
public class SettingsHandler {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;
    private final Gson gson = new Gson();

    /**
     * Constructor for the Meilisearch Settings object
     *
     * @param config Meilisearch configuration
     */
    public SettingsHandler(Config config) {
        meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Gets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws Exception if something goes wrong
     */
    public Settings getSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings"), Settings.class);
    }

    /**
     * Updates the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSettings(String uid, Settings settings) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings", settings.getUpdateQuery()),
                UpdateStatus.class);
    }

    /**
     * Resets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings"), UpdateStatus.class);
    }

    /**
     * Gets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#get-ranking-rules
     *
     * @param uid Index identifier
     * @return an array of strings that contains the ranking rules settings
     * @throws Exception if something goes wrong
     */
    public String[] getRankingRuleSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/ranking-rules"),
                String[].class);
    }

    /**
     * Updates the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#update-ranking-rules
     *
     * @param uid Index identifier
     * @param rankingRules the data that contains the new settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateRankingRuleSettings(String uid, String[] rankingRules)
            throws Exception {
        String rankingRulesAsJson = gson.toJson(rankingRules);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/ranking-rules", rankingRulesAsJson),
                UpdateStatus.class);
    }

    /**
     * Resets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#reset-ranking-rules
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetRankingRulesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/ranking-rules"),
                UpdateStatus.class);
    }

    /**
     * Gets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#get-synonyms
     *
     * @param uid Index identifier
     * @return a Map that contains all synonyms and their associated words
     * @throws Exception if something goes wrong
     */
    public Map<String, String[]> getSynonymsSettings(String uid) throws Exception {
        return this.gson.<Map<String, String[]>>fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/synonyms"), Map.class);
    }

    /**
     * Updates the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param uid Index identifier
     * @param synonyms a Map that contains the new synonyms settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSynonymsSettings(String uid, Map<String, String[]> synonyms)
            throws Exception {
        String synonymsAsJson = gson.toJson(synonyms);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/synonyms", synonymsAsJson),
                UpdateStatus.class);
    }

    /**
     * Resets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSynonymsSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/synonyms"),
                UpdateStatus.class);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @param uid Index identifier
     * @return an array of strings that contains the stop-words
     * @throws Exception if something goes wrong
     */
    public String[] getStopWordsSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/stop-words"),
                String[].class);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param uid Index identifier
     * @param stopWords an array of strings that contains the new stop-words settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateStopWordsSettings(String uid, String[] stopWords) throws Exception {
        String stopWordsAsJson = gson.toJson(stopWords);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/stop-words", stopWordsAsJson),
                UpdateStatus.class);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetStopWordsSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/stop-words"),
                UpdateStatus.class);
    }

    /**
     * Get the searchable attributes of an index.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains the searchable attributes
     * @throws Exception if something goes wrong
     */
    public String[] getSearchableAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/searchable-attributes"),
                String[].class);
    }

    /**
     * Updates the searchable attributes an index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param uid Index identifier
     * @param searchableAttributes an array of strings that contains the new searchable attributes
     *     settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSearchableAttributesSettings(
            String uid, String[] searchableAttributes) throws Exception {
        String searchableAttributesAsJson = gson.toJson(searchableAttributes);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/searchable-attributes",
                        searchableAttributesAsJson),
                UpdateStatus.class);
    }

    /**
     * Reset the searchable attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSearchableAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete(
                        "/indexes/" + uid + "/settings/searchable-attributes"),
                UpdateStatus.class);
    }

    /**
     * Get the display attributes of an index.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains attributes of an index to display
     * @throws Exception if something goes wrong
     */
    public String[] getDisplayedAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/displayed-attributes"),
                String[].class);
    }

    /**
     * Updates the display attributes of an index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param uid Index identifier
     * @param displayAttributes an array of strings that contains the new displayed attributes
     *     settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateDisplayedAttributesSettings(String uid, String[] displayAttributes)
            throws Exception {
        String displayAttributesAsJson = gson.toJson(displayAttributes);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/displayed-attributes",
                        displayAttributesAsJson),
                UpdateStatus.class);
    }

    /**
     * Reset the displayed attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetDisplayedAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/displayed-attributes"),
                UpdateStatus.class);
    }

    /**
     * Get an index's filterableAttributes.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains the filterable attributes settings
     * @throws Exception if something goes wrong
     */
    public String[] getFilterableAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/filterable-attributes"),
                String[].class);
    }

    /**
     * Update an index's filterable attributes list. This will re-index all documents in the index.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#update-filterable-attributes
     *
     * @param uid Index identifier
     * @param filterableAttributes an array of strings that contains the new filterable attributes
     *     settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateFilterableAttributesSettings(
            String uid, String[] filterableAttributes) throws Exception {
        String filterableAttributesAsJson = gson.toJson(filterableAttributes);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/filterable-attributes",
                        filterableAttributesAsJson),
                UpdateStatus.class);
    }

    /**
     * Reset an index's filterable attributes list back to its default value.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetFilterableAttributesSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete(
                        "/indexes/" + uid + "/settings/filterable-attributes"),
                UpdateStatus.class);
    }

    /**
     * Get the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @param uid Index identifier
     * @return a string of the distinct attribute field
     * @throws Exception if something goes wrong
     */
    public String getDistinctAttributeSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.get("/indexes/" + uid + "/settings/distinct-attribute"),
                String.class);
    }

    /**
     * Update the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param uid Index identifier
     * @param distinctAttribute a String that contains the new distinct attributes settings
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateDistinctAttributeSettings(String uid, String distinctAttribute)
            throws Exception {
        String distinctAttributeAsJson = gson.toJson(distinctAttribute);
        return this.gson.fromJson(
                meilisearchHttpRequest.post(
                        "/indexes/" + uid + "/settings/distinct-attribute",
                        distinctAttributeAsJson),
                UpdateStatus.class);
    }

    /**
     * Reset the distinct attribute field of an index to its default value.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#reset-distinct-attribute
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetDistinctAttributeSettings(String uid) throws Exception {
        return this.gson.fromJson(
                meilisearchHttpRequest.delete("/indexes/" + uid + "/settings/distinct-attribute"),
                UpdateStatus.class);
    }
}
