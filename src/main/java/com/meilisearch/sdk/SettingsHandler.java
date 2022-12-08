package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TypoTolerance;
import java.util.Map;

/**
 * Class covering the Meilisearch Settings API {@link Settings}
 *
 * <p>https://docs.meilisearch.com/reference/api/settings.html
 */
public class SettingsHandler {
    private final HttpClient httpClient;

    /**
     * Constructor for the Meilisearch Settings object
     *
     * @param config Meilisearch configuration
     */
    SettingsHandler(Config config) {
        httpClient = config.httpClient;
    }

    /**
     * Gets the settings of the index
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    Settings getSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.get(urlPath, Settings.class);
    }

    /**
     * Updates the settings of the index
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateSettings(String uid, Settings settings) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.post(urlPath, settings, Task.class);
    }

    /**
     * Resets the settings of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the ranking rules settings
     * @throws MeilisearchException if an error occurs
     */
    String[] getRankingRuleSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @param rankingRules the data that contains the new settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateRankingRuleSettings(String uid, String[] rankingRules) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.post(
                urlPath,
                rankingRules == null ? httpClient.jsonHandler.encode(rankingRules) : rankingRules,
                Task.class);
    }

    /**
     * Resets the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetRankingRulesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the synonyms settings of the index
     *
     * @param uid Index identifier
     * @return a Map that contains all synonyms and their associated words
     * @throws MeilisearchException if an error occurs
     */
    Map<String, String[]> getSynonymsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.jsonHandler.decode(httpClient.get(urlPath, String.class), Map.class);
    }

    /**
     * Updates the synonyms settings of the index
     *
     * @param uid Index identifier
     * @param synonyms a Map that contains the new synonyms settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateSynonymsSettings(String uid, Map<String, String[]> synonyms)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.post(
                urlPath,
                synonyms == null ? httpClient.jsonHandler.encode(synonyms) : synonyms,
                Task.class);
    }

    /**
     * Resets the synonyms settings of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetSynonymsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the stop-words settings of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the stop-words
     * @throws MeilisearchException if an error occurs
     */
    String[] getStopWordsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the stop-words settings of the index
     *
     * @param uid Index identifier
     * @param stopWords an array of strings that contains the new stop-words settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateStopWordsSettings(String uid, String[] stopWords) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.post(
                urlPath,
                stopWords == null ? httpClient.jsonHandler.encode(stopWords) : stopWords,
                Task.class);
    }

    /**
     * Resets the stop-words settings of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetStopWordsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the searchable attributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the searchable attributes
     * @throws MeilisearchException if an error occurs
     */
    String[] getSearchableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the searchable attributes of the index
     *
     * @param uid Index identifier
     * @param searchableAttributes an array of strings that contains the new searchable attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateSearchableAttributesSettings(String uid, String[] searchableAttributes)
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
     * Resets the searchable attributes of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetSearchableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the display attributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains attributes of the index to display
     * @throws MeilisearchException if an error occurs
     */
    String[] getDisplayedAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the display attributes of the index
     *
     * @param uid Index identifier
     * @param displayAttributes an array of strings that contains the new displayed attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateDisplayedAttributesSettings(String uid, String[] displayAttributes)
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
     * Resets the displayed attributes of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetDisplayedAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the filterableAttributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the filterable attributes settings
     * @throws MeilisearchException if an error occurs
     */
    String[] getFilterableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.get(urlPath, String[].class);
    }

    /**
     * Updates the filterable attributes of the index. This will re-index all documents in the index
     *
     * @param uid Index identifier
     * @param filterableAttributes an array of strings that contains the new filterable attributes
     *     settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateFilterableAttributesSettings(String uid, String[] filterableAttributes)
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
     * Resets the filterable attributes of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetFilterableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the distinct attribute field of the index
     *
     * @param uid Index identifier
     * @return a string of the distinct attribute field
     * @throws MeilisearchException if an error occurs
     */
    String getDistinctAttributeSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        String response = httpClient.get(urlPath, String.class);
        return response.equals("null") ? null : response.substring(1, response.length() - 1);
    }

    /**
     * Updates the distinct attribute field of the index
     *
     * @param uid Index identifier
     * @param distinctAttribute a String that contains the new distinct attributes settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateDistinctAttributeSettings(String uid, String distinctAttribute)
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
     * Resets the distinct attribute field of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetDistinctAttributeSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        return httpClient.delete(urlPath, Task.class);
    }

    /**
     * Gets the typo tolerance settings of the index
     *
     * @param uid Index identifier
     * @return a TypoTolerance instance that contains all typo tolerance settings
     * @throws MeilisearchException if an error occurs
     */
    TypoTolerance getTypoToleranceSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.get(urlPath, TypoTolerance.class);
    }

    /**
     * Updates the typo tolerance settings of the index
     *
     * @param uid Index identifier
     * @param typoTolerance a TypoTolerance instance that contains the new typo tolerance settings
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task updateTypoToleranceSettings(String uid, TypoTolerance typoTolerance)
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
     * Resets the typo tolerance settings of the index
     *
     * @param uid Index identifier
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    Task resetTypoToleranceSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.delete(urlPath, Task.class);
    }
}
