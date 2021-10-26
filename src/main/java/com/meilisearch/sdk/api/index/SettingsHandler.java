package com.meilisearch.sdk.api.index;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.UpdateStatus;
import com.meilisearch.sdk.api.documents.Update;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import java.util.Collections;
import java.util.Map;

public class SettingsHandler {
    private final ServiceTemplate serviceTemplate;
    private final RequestFactory requestFactory;

    public SettingsHandler(ServiceTemplate serviceTemplate, RequestFactory requestFactory) {
        this.serviceTemplate = serviceTemplate;
        this.requestFactory = requestFactory;
    }

    /**
     * Gets the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param settings the data that contains the new settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateSettings(String uid, Settings settings) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), settings),
                Update.class);
    }

    /**
     * Resets the settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Gets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#get-settings
     *
     * @param uid Index identifier
     * @return ranking rules settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getRankingRulesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#update-settings
     *
     * @param uid Index identifier
     * @param rankingRulesSettings the data that contains the new ranking rules settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateRankingRulesSettings(String uid, String[] rankingRulesSettings)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST,
                        requestQuery,
                        Collections.emptyMap(),
                        rankingRulesSettings),
                Update.class);
    }

    /**
     * Resets the ranking rules settings of a given index Refer
     * https://docs.meilisearch.com/references/settings.html#reset-settings
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetRankingRulesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/ranking-rules";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Gets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#get-synonyms
     *
     * @param uid Index identifier
     * @return a Map that contains all synonyms and their associated words
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getSynonymsSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/synonyms";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param uid Index identifier
     * @param synonyms a Map that contains the new synonyms settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateSynonymsSettings(String uid, Map<String, String[]> synonyms)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/synonyms";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), synonyms),
                Update.class);
    }

    /**
     * Resets the synonyms settings of a given index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetSynonymsSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/synonyms";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @param uid Index identifier
     * @return An array of strings that contains the stop-words.
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getStopWordsSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/stop-words";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param uid Index identifier
     * @param stopWords the data that contains the new ranking rules settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateStopWordsSettings(String uid, String[] stopWords)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/stop-words";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), stopWords),
                Update.class);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetStopWordsSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/stop-words";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Get the searchable attributes of an index.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @param uid Index identifier
     * @return searchable attributes of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getSearchableAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/searchable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the searchable attributes an index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param uid Index identifier
     * @param searchableAttributes the data that contains the new ranking rules settings
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateSearchableAttributesSettings(String uid, String[] searchableAttributes)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/searchable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST,
                        requestQuery,
                        Collections.emptyMap(),
                        searchableAttributes),
                Update.class);
    }

    /**
     * Reset the searchable attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetSearchableAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/searchable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Get the display attributes of an index.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @param uid Index identifier
     * @return display attributes settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getDisplayedAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/displayed-attributes";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Updates the display attributes of an index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param uid Index identifier
     * @param displayAttributes An array of strings that contains attributes of an index to display
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateDisplayedAttributesSettings(String uid, String[] displayAttributes)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/displayed-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), displayAttributes),
                Update.class);
    }

    /**
     * Reset the displayed attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetDisplayedAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/displayed-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Get an index's filterableAttributes.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @param uid Index identifier
     * @return filterable attributes settings of a given uid as String
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Settings getFilterableAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/filterable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Update an index's filterable attributes list. This will re-index all documents in the index.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#update-filterable-attributes
     *
     * @param uid Index identifier
     * @param filterableAttributes An array of strings containing the attributes that can be used as
     *     filters at query time.
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update updateFilterableAttributesSettings(String uid, String[] filterableAttributes)
            throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/filterable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST,
                        requestQuery,
                        Collections.emptyMap(),
                        filterableAttributes),
                Update.class);
    }

    /**
     * Reset an index's filterable attributes list back to its default value.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @param uid Index identifier
     * @return updateId is the id of the update
     * @throws MeiliSearchRuntimeException if something goes wrong
     */
    public Update resetFilterableAttributesSettings(String uid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + uid + "/settings/filterable-attributes";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }

    /**
     * Get the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @param uid Index identifier
     * @return distinct attribute field of a given uid as String
     * @throws Exception if something goes wrong
     */
    public Settings getDistinctAttributeSettings(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/settings/distinct-attribute";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Settings.class);
    }

    /**
     * Update the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param uid Index identifier
     * @param distinctAttribute A String: the field name.
     * @return updateId is the id of the update
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateDistinctAttributeSettings(String uid, String distinctAttribute)
            throws Exception {
        String requestQuery = "/indexes/" + uid + "/settings/distinct-attribute";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.POST, requestQuery, Collections.emptyMap(), distinctAttribute),
                Update.class);
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
        String requestQuery = "/indexes/" + uid + "/settings/distinct-attribute";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Update.class);
    }
}
