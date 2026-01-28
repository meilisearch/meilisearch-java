package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.Embedder;
import com.meilisearch.sdk.model.Faceting;
import com.meilisearch.sdk.model.FilterableAttributesConfig;
import com.meilisearch.sdk.model.FilterableAttributesLegacyAdapter;
import com.meilisearch.sdk.model.LocalizedAttribute;
import com.meilisearch.sdk.model.Pagination;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TypoTolerance;
import java.util.Map;

/**
 * Class covering the Meilisearch Settings API
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/settings">API specification</a>
 * @see Settings
 */
public class SettingsHandler {
    private final HttpClient httpClient;

    /**
     * Constructor for the Meilisearch Settings object
     *
     * @param config Meilisearch configuration
     */
    protected SettingsHandler(Config config) {
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
        return httpClient.get(settingsPath(uid).getURL(), Settings.class);
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
        return httpClient.patch(settingsPath(uid).getURL(), settings, TaskInfo.class);
    }

    /**
     * Resets the settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSettings(String uid) throws MeilisearchException {
        return httpClient.delete(settingsPath(uid).getURL(), TaskInfo.class);
    }

    /**
     * Gets the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the ranking rules settings
     * @throws MeilisearchException if an error occurs
     */
    String[] getRankingRulesSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("ranking-rules").getURL(), String[].class);
    }

    /**
     * Updates the ranking rules settings of the index
     *
     * @param uid Index identifier
     * @param rankingRules the data that contains the new settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateRankingRulesSettings(String uid, String[] rankingRules)
            throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("ranking-rules").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("ranking-rules").getURL(), TaskInfo.class);
    }

    /**
     * Gets the synonyms settings of the index
     *
     * @param uid Index identifier
     * @return a Map that contains all synonyms and their associated words
     * @throws MeilisearchException if an error occurs
     */
    Map<String, String[]> getSynonymsSettings(String uid) throws MeilisearchException {
        return httpClient.jsonHandler.decode(
                httpClient.get(settingsPath(uid).addSubroute("synonyms").getURL(), String.class),
                Map.class);
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
        return httpClient.put(
                settingsPath(uid).addSubroute("synonyms").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("synonyms").getURL(), TaskInfo.class);
    }

    /**
     * Gets the stop-words settings of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the stop-words
     * @throws MeilisearchException if an error occurs
     */
    String[] getStopWordsSettings(String uid) throws MeilisearchException {
        return httpClient.get(settingsPath(uid).addSubroute("stop-words").getURL(), String[].class);
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
        return httpClient.put(
                settingsPath(uid).addSubroute("stop-words").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("stop-words").getURL(), TaskInfo.class);
    }

    /**
     * Gets the searchable attributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the searchable attributes
     * @throws MeilisearchException if an error occurs
     */
    String[] getSearchableAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("searchable-attributes").getURL(), String[].class);
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
        return httpClient.put(
                settingsPath(uid).addSubroute("searchable-attributes").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("searchable-attributes").getURL(), TaskInfo.class);
    }

    /**
     * Gets the display attributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains attributes of the index to display
     * @throws MeilisearchException if an error occurs
     */
    String[] getDisplayedAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("displayed-attributes").getURL(), String[].class);
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
        return httpClient.put(
                settingsPath(uid).addSubroute("displayed-attributes").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("displayed-attributes").getURL(), TaskInfo.class);
    }

    /**
     * Gets the localized attributes of the index
     *
     * @param uid Index identifier
     * @return an array of localizedattributes that contains attributes of the index to display
     * @throws MeilisearchException if an error occurs
     */
    LocalizedAttribute[] getLocalizedAttributes(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("localized-attributes").getURL(),
                LocalizedAttribute[].class);
    }

    /**
     * Updates the localized attributes of the index.
     *
     * @param uid Index identifier
     * @param localizedAttributes an array of LocalizedAttributes that contain patterns and locales
     *     settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateLocalizedAttributesSettings(String uid, LocalizedAttribute[] localizedAttributes)
            throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("localized-attributes").getURL(),
                localizedAttributes == null
                        ? httpClient.jsonHandler.encode(localizedAttributes)
                        : localizedAttributes,
                TaskInfo.class);
    }

    /**
     * Resets the localized attributes of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetLocalizedAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("localized-attributes").getURL(), TaskInfo.class);
    }

    /**
     * Gets the filterableAttributes of the index (legacy String[] view).
     *
     * @param uid Index identifier
     * @return an array of strings that contains the filterable attributes settings
     * @throws MeilisearchException if an error occurs or granular configs cannot be reduced
     */
    String[] getFilterableAttributesSettings(String uid) throws MeilisearchException {
        return FilterableAttributesLegacyAdapter.toLegacyNamesOrThrow(
                getGranularFilterableAttributesSettings(uid));
    }

    /**
     * Gets the filterableAttributes of the index (granular view).
     *
     * @param uid Index identifier
     * @return an array of filterable attribute configs (strings or granular objects)
     * @throws MeilisearchException if an error occurs
     */
    FilterableAttributesConfig[] getGranularFilterableAttributesSettings(String uid)
            throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("filterable-attributes").getURL(),
                FilterableAttributesConfig[].class);
    }

    /**
     * Updates the filterable attributes of the index using the legacy String[] view. This will
     * re-index all documents in the index.
     *
     * @param uid Index identifier
     * @param filterableAttributes an array of strings that contains the new filterable attributes
     *     settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateFilterableAttributesSettings(String uid, String[] filterableAttributes)
            throws MeilisearchException {
        return updateGranularFilterableAttributesSettings(
                uid, FilterableAttributesLegacyAdapter.fromLegacyNames(filterableAttributes));
    }

    /**
     * Updates the filterable attributes of the index using the granular representation. This will
     * re-index all documents in the index.
     *
     * @param uid Index identifier
     * @param filterableAttributes Array of filterable attribute configs (strings or granular
     *     objects) that contains the new filterable attributes settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateGranularFilterableAttributesSettings(
            String uid, FilterableAttributesConfig[] filterableAttributes)
            throws MeilisearchException {
        Object body =
                filterableAttributes == null
                        ? httpClient.jsonHandler.encode(filterableAttributes)
                        : filterableAttributes;
        return httpClient.put(
                settingsPath(uid).addSubroute("filterable-attributes").getURL(),
                body,
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("filterable-attributes").getURL(), TaskInfo.class);
    }

    /**
     * Gets the sortableAttributes of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the sortable attributes settings
     * @throws MeilisearchException if an error occurs
     */
    String[] getSortableAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("sortable-attributes").getURL(), String[].class);
    }

    /**
     * Updates the sortable attributes of the index. This will re-index all documents in the index
     *
     * @param uid Index identifier
     * @param sortableAttributes an array of strings that contains the new sortable attributes
     *     settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateSortableAttributesSettings(String uid, String[] sortableAttributes)
            throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("sortable-attributes").getURL(),
                sortableAttributes == null
                        ? httpClient.jsonHandler.encode(sortableAttributes)
                        : sortableAttributes,
                TaskInfo.class);
    }
    /**
     * Resets the sortable attributes of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSortableAttributesSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("sortable-attributes").getURL(), TaskInfo.class);
    }

    /**
     * Gets the distinct attribute field of the index
     *
     * @param uid Index identifier
     * @return a string of the distinct attribute field
     * @throws MeilisearchException if an error occurs
     */
    String getDistinctAttributeSettings(String uid) throws MeilisearchException {
        String response =
                httpClient.get(
                        settingsPath(uid).addSubroute("distinct-attribute").getURL(), String.class);
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
        return httpClient.put(
                settingsPath(uid).addSubroute("distinct-attribute").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("distinct-attribute").getURL(), TaskInfo.class);
    }

    /**
     * Gets the typo tolerance settings of the index
     *
     * @param uid Index identifier
     * @return a TypoTolerance instance that contains all typo tolerance settings
     * @throws MeilisearchException if an error occurs
     */
    TypoTolerance getTypoToleranceSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("typo-tolerance").getURL(), TypoTolerance.class);
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
        return httpClient.patch(
                settingsPath(uid).addSubroute("typo-tolerance").getURL(),
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
        return httpClient.delete(
                settingsPath(uid).addSubroute("typo-tolerance").getURL(), TaskInfo.class);
    }

    /**
     * Gets the pagination settings of the index
     *
     * @param uid Index identifier
     * @return a Pagination instance that contains all pagination settings
     * @throws MeilisearchException if an error occurs
     */
    Pagination getPaginationSettings(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("pagination").getURL(), Pagination.class);
    }

    /**
     * Updates the pagination settings of the index
     *
     * @param uid Index identifier
     * @param pagination a Pagination instance that contains the new pagination settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updatePaginationSettings(String uid, Pagination pagination)
            throws MeilisearchException {
        return httpClient.patch(
                settingsPath(uid).addSubroute("pagination").getURL(),
                pagination == null ? httpClient.jsonHandler.encode(pagination) : pagination,
                TaskInfo.class);
    }

    /**
     * Resets the pagination settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetPaginationSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("pagination").getURL(), TaskInfo.class);
    }

    /**
     * Gets the faceting settings of the index
     *
     * @param uid Index identifier
     * @return a Faceting instance that contains all faceting settings
     * @throws MeilisearchException if an error occurs
     */
    Faceting getFacetingSettings(String uid) throws MeilisearchException {
        return httpClient.get(settingsPath(uid).addSubroute("faceting").getURL(), Faceting.class);
    }

    /**
     * Updates the pagination settings of the index
     *
     * @param uid Index identifier
     * @param faceting a Faceting instance that contains the new faceting settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateFacetingSettings(String uid, Faceting faceting) throws MeilisearchException {
        return httpClient.patch(
                settingsPath(uid).addSubroute("faceting").getURL(),
                faceting == null ? httpClient.jsonHandler.encode(faceting) : faceting,
                TaskInfo.class);
    }

    /**
     * Reset the faceting settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetFacetingSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("faceting").getURL(), TaskInfo.class);
    }

    /**
     * Gets the dictionary settings of the index
     *
     * @param uid Index identifier
     * @return an array of strings that contains the dictionary
     * @throws MeilisearchException if an error occurs
     */
    String[] getDictionarySettings(String uid) throws MeilisearchException {
        return httpClient.get(settingsPath(uid).addSubroute("dictionary").getURL(), String[].class);
    }

    /**
     * Updates the dictionary settings of the index
     *
     * @param uid Index identifier
     * @param dictionary an array of strings that contains the new dictionary settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateDictionarySettings(String uid, String[] dictionary) throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("dictionary").getURL(),
                dictionary == null ? httpClient.jsonHandler.encode(dictionary) : dictionary,
                TaskInfo.class);
    }

    /**
     * Resets the dictionary settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetDictionarySettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("dictionary").getURL(), TaskInfo.class);
    }

    /**
     * Gets the proximity precision level of the index
     *
     * @param uid Index identifier
     * @return a string of the proximity precision level
     * @throws MeilisearchException if an error occurs
     */
    String getProximityPrecisionSettings(String uid) throws MeilisearchException {
        String response =
                httpClient.get(
                        settingsPath(uid).addSubroute("proximity-precision").getURL(),
                        String.class);
        return response.substring(1, response.length() - 1);
    }

    /**
     * Updates the proximity precision level of the index
     *
     * @param uid Index identifier
     * @param proximityPrecision a String that contains the new proximity precision level settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateProximityPrecisionSettings(String uid, String proximityPrecision)
            throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("proximity-precision").getURL(),
                proximityPrecision == null
                        ? httpClient.jsonHandler.encode(proximityPrecision)
                        : "\"" + proximityPrecision + "\"",
                TaskInfo.class);
    }

    /**
     * Resets the proximity precision level of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetProximityPrecisionSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("proximity-precision").getURL(), TaskInfo.class);
    }

    /** Creates an URLBuilder for the constant route settings */
    private URLBuilder settingsPath(String uid) {
        return new URLBuilder("/indexes").addSubroute(uid).addSubroute("/settings");
    }

    /**
     * Gets the search cutoff value of the index
     *
     * @param uid Index identifier
     * @return an integer of the search cutoff value
     * @throws MeilisearchException if an error occurs
     */
    Integer getSearchCutoffMsSettings(String uid) throws MeilisearchException {
        String response =
                httpClient.get(
                        settingsPath(uid).addSubroute("search-cutoff-ms").getURL(), String.class);
        return response.equals("null") ? null : Integer.valueOf(response);
    }

    /**
     * Updates the search cutoff value of the index
     *
     * @param uid Index identifier
     * @param milliseconds an Integer that contains the new search cutoff value
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateSearchCutoffMsSettings(String uid, Integer milliseconds)
            throws MeilisearchException {
        return httpClient.put(
                settingsPath(uid).addSubroute("search-cutoff-ms").getURL(),
                milliseconds == null ? httpClient.jsonHandler.encode(milliseconds) : milliseconds,
                TaskInfo.class);
    }

    /**
     * Resets the search cutoff value of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetSearchCutoffMsSettings(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("search-cutoff-ms").getURL(), TaskInfo.class);
    }

    /**
     * Gets the separator tokens of the index
     *
     * @param uid Index identifier
     * @return separator tokens of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getSeparatorTokensSettings(String uid) {
        return httpClient.get(
                settingsPath(uid).addSubroute("separator-tokens").getURL(), String[].class);
    }

    /**
     * Updates the separator tokens settings of the index
     *
     * @param uid Index identifier
     * @param separatorTokens an array of strings that contains the new separator tokens settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateSeparatorTokensSettings(String uid, String[] separatorTokens) {
        return httpClient.put(
                settingsPath(uid).addSubroute("separator-tokens").getURL(),
                separatorTokens == null
                        ? httpClient.jsonHandler.encode(separatorTokens)
                        : separatorTokens,
                TaskInfo.class);
    }

    /**
     * Resets the separator tokens settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetSeparatorTokensSettings(String uid) {
        return httpClient.delete(
                settingsPath(uid).addSubroute("separator-tokens").getURL(), TaskInfo.class);
    }

    /**
     * Gets the non-separator tokens of the index
     *
     * @param uid Index identifier
     * @return non-separator tokens of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getNonSeparatorTokensSettings(String uid) {
        return httpClient.get(
                settingsPath(uid).addSubroute("non-separator-tokens").getURL(), String[].class);
    }

    /**
     * Updates the non-separator tokens settings of the index
     *
     * @param uid Index identifier
     * @param separatorTokens an array of strings that contains the new separator tokens settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateNonSeparatorTokensSettings(String uid, String[] separatorTokens) {
        return httpClient.put(
                settingsPath(uid).addSubroute("non-separator-tokens").getURL(),
                separatorTokens == null
                        ? httpClient.jsonHandler.encode(separatorTokens)
                        : separatorTokens,
                TaskInfo.class);
    }

    /**
     * Resets the non-separator tokens settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetNonSeparatorTokensSettings(String uid) {
        return httpClient.delete(
                settingsPath(uid).addSubroute("non-separator-tokens").getURL(), TaskInfo.class);
    }

    /**
     * Gets the embedders settings of the index
     *
     * @param uid Index identifier
     * @return a Map that contains all embedders settings
     * @throws MeilisearchException if an error occurs
     */
    Map<String, Embedder> getEmbedders(String uid) throws MeilisearchException {
        return httpClient.get(
                settingsPath(uid).addSubroute("embedders").getURL(),
                Map.class,
                String.class,
                Embedder.class);
    }

    /**
     * Updates the embedders settings of the index
     *
     * @param uid Index identifier
     * @param embedders a Map that contains the new embedders settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo updateEmbedders(String uid, Map<String, Embedder> embedders)
            throws MeilisearchException {
        return httpClient.patch(
                settingsPath(uid).addSubroute("embedders").getURL(),
                embedders == null ? null : httpClient.jsonHandler.encode(embedders),
                TaskInfo.class);
    }

    /**
     * Resets the embedders settings of the index
     *
     * @param uid Index identifier
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    TaskInfo resetEmbedders(String uid) throws MeilisearchException {
        return httpClient.delete(
                settingsPath(uid).addSubroute("embedders").getURL(), TaskInfo.class);
    }
}
