package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateSettings(String uid, Settings settings) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.patch(urlPath, settings, TaskInfo.class);
    }

    /**
     * Resets the settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateRankingRuleSettings(String uid, String[] rankingRules)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.put(
                urlPath,
                rankingRules == null ? httpClient.jsonHandler.encode(rankingRules) : rankingRules,
                TaskInfo.class);
    }

    /**
     * Resets the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetRankingRulesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/ranking-rules";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateSynonymsSettings(String uid, Map<String, String[]> synonyms)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.put(
                urlPath,
                synonyms == null ? httpClient.jsonHandler.encode(synonyms) : synonyms,
                TaskInfo.class);
    }

    /**
     * Resets the synonyms settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSynonymsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/synonyms";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateStopWordsSettings(String uid, String[] stopWords) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.put(
                urlPath,
                stopWords == null ? httpClient.jsonHandler.encode(stopWords) : stopWords,
                TaskInfo.class);
    }

    /**
     * Resets the stop-words settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetStopWordsSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/stop-words";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateSearchableAttributesSettings(String uid, String[] searchableAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.put(
                urlPath,
                searchableAttributes == null
                        ? httpClient.jsonHandler.encode(searchableAttributes)
                        : searchableAttributes,
                TaskInfo.class);
    }

    /**
     * Resets the searchable attributes of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSearchableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/searchable-attributes";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateDisplayedAttributesSettings(String uid, String[] displayAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.put(
                urlPath,
                displayAttributes == null
                        ? httpClient.jsonHandler.encode(displayAttributes)
                        : displayAttributes,
                TaskInfo.class);
    }

    /**
     * Resets the displayed attributes of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetDisplayedAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/displayed-attributes";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateFilterableAttributesSettings(String uid, String[] filterableAttributes)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.put(
                urlPath,
                filterableAttributes == null
                        ? httpClient.jsonHandler.encode(filterableAttributes)
                        : filterableAttributes,
                TaskInfo.class);
    }

    /**
     * Resets the filterable attributes of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetFilterableAttributesSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/filterable-attributes";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateDistinctAttributeSettings(String uid, String distinctAttribute)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        return httpClient.put(
                urlPath,
                distinctAttribute == null
                        ? httpClient.jsonHandler.encode(distinctAttribute)
                        : "\"" + distinctAttribute + "\"",
                TaskInfo.class);
    }

    /**
     * Resets the distinct attribute field of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetDistinctAttributeSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/distinct-attribute";
        return httpClient.delete(urlPath, TaskInfo.class);
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
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateTypoToleranceSettings(String uid, TypoTolerance typoTolerance)
            throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.patch(
                urlPath,
                typoTolerance == null
                        ? httpClient.jsonHandler.encode(typoTolerance)
                        : typoTolerance,
                TaskInfo.class);
    }

    /**
     * Resets the typo tolerance settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetTypoToleranceSettings(String uid) throws MeilisearchException {
        String urlPath = "/indexes/" + uid + "/settings/typo-tolerance";
        return httpClient.delete(urlPath, TaskInfo.class);
    }
}
