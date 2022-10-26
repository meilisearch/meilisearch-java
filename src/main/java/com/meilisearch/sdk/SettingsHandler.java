package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TypoTolerance;
import java.util.Map;

/**
 * Settings Handler for manipulation of an Index {@link Settings}
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/settings.html
 */
public class SettingsHandler {
    private final HttpClient httpClient;

    /**
     * Constructor for the Meilisearch Settings object
     *
     * @param config Meilisearch configuration
     */
    public SettingsHandler(Config config) {
        httpClient = config.httpClient;
    }

    /**
     * Gets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public Settings getSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.get(urlPath, Settings.class);
    }

    /**
     * Updates the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateSettings(String uid, Settings settings) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.post(urlPath, settings, Task.class);
    }

    /**
     * Resets the settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings"), Task.class);
    }

    /**
     * Gets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#get-ranking-rules
     *
     * @param uid Index identifier
     * @return an array of strings that contains the ranking rules settings
     * @throws MeilisearchException if an error occurs
     */
    public String[] getRankingRuleSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#update-ranking-rules
     *
     * @param uid Index identifier
     * @param rankingRules the data that contains the new settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateRankingRuleSettings(String uid, String[] rankingRules)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.post(
                urlPath,
                rankingRules == null ? httpClient.jsonHandler.encode(rankingRules) : rankingRules,
                Task.class);
    }

    /**
     * Resets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/ranking_rules.html#reset-ranking-rules
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetRankingRulesSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/ranking-rules"), Task.class);
    }

    /**
     * Gets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#get-synonyms
     *
     * @param uid Index identifier
     * @return a Map that contains all synonyms and their associated words
     * @throws MeilisearchException if an error occurs
     */
    public Map<String, String[]> getSynonymsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.jsonHandler.decode(httpClient.get(urlPath, String.class), Map.class);
    }

    /**
     * Updates the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param uid Index identifier
     * @param synonyms a Map that contains the new synonyms settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateSynonymsSettings(String uid, Map<String, String[]> synonyms)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.post(
                urlPath,
                synonyms == null ? httpClient.jsonHandler.encode(synonyms) : synonyms,
                Task.class);
    }

    /**
     * Resets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetSynonymsSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/synonyms"), Task.class);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @param uid Index identifier
     * @return an array of strings that contains the stop-words
     * @throws MeilisearchException if an error occurs
     */
    public String[] getStopWordsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param uid Index identifier
     * @param stopWords an array of strings that contains the new stop-words settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateStopWordsSettings(String uid, String[] stopWords)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.post(
                urlPath,
                stopWords == null ? httpClient.jsonHandler.encode(stopWords) : stopWords,
                Task.class);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetStopWordsSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/stop-words"), Task.class);
    }

    /**
     * Get the searchable attributes of an index.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains the searchable attributes
     * @throws MeilisearchException if an error occurs
     */
    public String[] getSearchableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the searchable attributes an index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param uid Index identifier
     * @param searchableAttributes an array of strings that contains the new searchable attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateSearchableAttributesSettings(String uid, String[] searchableAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.post(
                urlPath,
                searchableAttributes == null
                        ? httpClient.jsonHandler.encode(searchableAttributes)
                        : searchableAttributes,
                Task.class);
    }

    /**
     * Reset the searchable attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetSearchableAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/searchable-attributes"),
                Task.class);
    }

    /**
     * Get the display attributes of an index.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains attributes of an index to display
     * @throws MeilisearchException if an error occurs
     */
    public String[] getDisplayedAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the display attributes of an index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param uid Index identifier
     * @param displayAttributes an array of strings that contains the new displayed attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateDisplayedAttributesSettings(String uid, String[] displayAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.post(
                urlPath,
                displayAttributes == null
                        ? httpClient.jsonHandler.encode(displayAttributes)
                        : displayAttributes,
                Task.class);
    }

    /**
     * Reset the displayed attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetDisplayedAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/displayed-attributes"),
                Task.class);
    }

    /**
     * Get an index's filterableAttributes.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @param uid Index identifier
     * @return an array of strings that contains the filterable attributes settings
     * @throws MeilisearchException if an error occurs
     */
    public String[] getFilterableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Update an index's filterable attributes list. This will re-index all documents in the index.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#update-filterable-attributes
     *
     * @param uid Index identifier
     * @param filterableAttributes an array of strings that contains the new filterable attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateFilterableAttributesSettings(String uid, String[] filterableAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.post(
                urlPath,
                filterableAttributes == null
                        ? httpClient.jsonHandler.encode(filterableAttributes)
                        : filterableAttributes,
                Task.class);
    }

    /**
     * Reset an index's filterable attributes list back to its default value.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetFilterableAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/filterable-attributes"),
                Task.class);
    }

    /**
     * Get the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @param uid Index identifier
     * @return a string of the distinct attribute field
     * @throws MeilisearchException if an error occurs
     */
    public String getDistinctAttributeSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        String response = httpClient.get(urlPath, String.class);
        return response.equals("null") ? null : response.substring(1, response.length() - 1);
    }

    /**
     * Update the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param uid Index identifier
     * @param distinctAttribute a String that contains the new distinct attributes settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateDistinctAttributeSettings(String uid, String distinctAttribute)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        return httpClient.post(
                urlPath,
                distinctAttribute == null
                        ? httpClient.jsonHandler.encode(distinctAttribute)
                        : "\"" + distinctAttribute + "\"",
                Task.class);
    }

    /**
     * Reset the distinct attribute field of an index to its default value.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#reset-distinct-attribute
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetDistinctAttributeSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/distinct-attribute"), Task.class);
    }

    /**
     * Gets the typo tolerance settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#get-typo-tolerance
     *
     * @param uid Index identifier
     * @return a TypoTolerance instance that contains all typo tolerance settings
     * @throws MeilisearchException if an error occurs
     */
    public TypoTolerance getTypoToleranceSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.get(urlPath, TypoTolerance.class);
    }

    /**
     * Updates the typo tolerance settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#update-typo-tolerance
     *
     * @param uid Index identifier
     * @param typoTolerance a TypoTolerance instance that contains the new typo tolerance settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task updateTypoToleranceSettings(String uid, TypoTolerance typoTolerance)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.post(
                urlPath,
                typoTolerance == null
                        ? httpClient.jsonHandler.encode(typoTolerance)
                        : typoTolerance,
                Task.class);
    }

    /**
     * Resets the typo tolerance settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#reset-typo-tolerance
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task resetTypoToleranceSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.delete("/indexes/" + uid + "/settings/typo-tolerance"), Task.class);
    }
}
