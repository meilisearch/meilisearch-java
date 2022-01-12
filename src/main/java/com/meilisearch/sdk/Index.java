package com.meilisearch.sdk;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.meilisearch.sdk.model.SearchResult;
import java.io.Serializable;
import java.util.*;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

/** MeiliSearch index */
@ToString
public class Index implements Serializable {
    @Getter String uid;

    @Getter String primaryKey;

    @Getter String createdAt;

    @Getter @ToString.Exclude String updatedAt;

    @Getter @ToString.Exclude Config config;

    @ToString.Exclude Documents documents;

    @ToString.Exclude Updates updates;

    @ToString.Exclude Search search;

    @ToString.Exclude SettingsHandler settingsHandler;

    Gson gson = new Gson();

    /**
     * Sets the MeiliSearch configuration for the index
     *
     * @param config MeiliSearch configuration to use
     */
    void setConfig(Config config) {
        this.config = config;
        this.documents = new Documents(config);
        this.updates = new Updates(config);
        this.search = new Search(config);
        this.settingsHandler = new SettingsHandler(config);
    }

    /**
     * Gets a document in the index
     *
     * @param identifier Identifier of the document to get
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String getDocument(String identifier) throws Exception {
        return this.documents.getDocument(this.uid, identifier);
    }

    /**
     * Gets documents in the index
     *
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String getDocuments() throws Exception {
        return this.documents.getDocuments(this.uid);
    }

    /**
     * Gets documents in the index and limit the number of documents returned
     *
     * @param limits Maximum amount of documents to return
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String getDocuments(int limits) throws Exception {
        return this.documents.getDocuments(this.uid, limits);
    }

    /**
     * Gets documents in the index and limit the number of documents returned
     *
     * @param limits Maximum amount of documents to return
     * @param offset Number of documents to skip
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String getDocuments(int limits, int offset) throws Exception {
        return this.documents.getDocuments(this.uid, limits, offset);
    }

    /**
     * Gets documents in the index and limit the number of documents returned
     *
     * @param limits Maximum amount of documents to return
     * @param offset Number of documents to skip
     * @param attributesToRetrieve Document attributes to show
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String getDocuments(int limits, int offset, List<String> attributesToRetrieve)
            throws Exception {
        return this.documents.getDocuments(this.uid, limits, offset, attributesToRetrieve);
    }

    /**
     * Adds a document in the index
     *
     * @param document Document to add in JSON string format
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocuments(String document) throws Exception {
        return this.documents.addDocuments(this.uid, document, null);
    }

    /**
     * Adds a document in the index
     *
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocuments(String document, String primaryKey) throws Exception {
        return this.documents.addDocuments(this.uid, document, primaryKey);
    }

    /**
     * Adds a document in the index in batches
     *
     * @param batchSize size of the batch of documents
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsInBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        JSONArray arrayResponses = new JSONArray();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.put(
                    new JSONObject(
                            this.documents.addDocuments(
                                    this.uid, jsonSubArray.toString(), primaryKey)));
        }
        return arrayResponses.toString();
    }

    /**
     * Add Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsInBatches(String document) throws Exception {
        return this.addDocumentsInBatches(document, 1000, null);
    }

    /**
     * Updates a document in the index
     *
     * @param document Document to update in JSON string format
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocuments(String document) throws Exception {
        return this.documents.updateDocuments(this.uid, document, null);
    }

    /**
     * Updates a document in the index
     *
     * @param document Document to update in JSON string format
     * @param primaryKey PrimaryKey of the document
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocuments(String document, String primaryKey) throws Exception {
        return this.documents.updateDocuments(this.uid, document, primaryKey);
    }

    /**
     * Update Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the document to add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsInBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        JSONArray arrayResponses = new JSONArray();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.put(
                    new JSONObject(
                            this.documents.updateDocuments(
                                    this.uid, jsonSubArray.toString(), primaryKey)));
        }
        return arrayResponses.toString();
    }

    /**
     * Update Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsInBatches(String document) throws Exception {
        return this.updateDocumentsInBatches(document, 1000, null);
    }

    /**
     * Deletes a document from the index
     *
     * @param identifier Identifier of the document to delete
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String deleteDocument(String identifier) throws Exception {
        return this.documents.deleteDocument(this.uid, identifier);
    }

    /**
     * Deletes list of documents from the index
     *
     * @param documentsIdentifiers list of identifiers of documents to delete
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String deleteDocuments(List<String> documentsIdentifiers) throws Exception {
        return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
    }

    /**
     * Deletes all documents in the index
     *
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String deleteAllDocuments() throws Exception {
        return this.documents.deleteAllDocuments(this.uid);
    }

    /**
     * Searches documents in index
     *
     * @param q Query string
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public SearchResult search(String q) throws Exception {
        return this.search.search(this.uid, q);
    }

    /**
     * Searches documents in index
     *
     * @param searchRequest SearchRequest SearchRequest
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public SearchResult search(SearchRequest searchRequest) throws Exception {
        return this.search.search(this.uid, searchRequest);
    }

    public String rawSearch(String query) throws Exception {
        return this.search.rawSearch(this.uid, query);
    }

    public String rawSearch(SearchRequest searchRequest) throws Exception {
        return this.search.rawSearch(this.uid, searchRequest);
    }

    /**
     * Gets the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return settings of a given uid as String
     * @throws Exception if something goes wrong
     */
    public Settings getSettings() throws Exception {
        return this.settingsHandler.getSettings(this.uid);
    }

    /**
     * Updates the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param settings the object that contains the data with the new settings
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSettings(Settings settings) throws Exception {
        return this.settingsHandler.updateSettings(this.uid, settings);
    }

    /**
     * Resets the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSettings() throws Exception {
        return this.settingsHandler.resetSettings(this.uid);
    }

    /**
     * Gets the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return ranking rules of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getRankingRuleSettings() throws Exception {
        return this.settingsHandler.getRankingRuleSettings(this.uid);
    }

    /**
     * Updates the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param rankingRules array that contain the data with the new ranking rules
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateRankingRuleSettings(String[] rankingRules) throws Exception {
        return this.settingsHandler.updateRankingRuleSettings(this.uid, rankingRules);
    }

    /**
     * Resets the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetRankingRuleSettings() throws Exception {
        return this.settingsHandler.resetRankingRulesSettings(this.uid);
    }

    /**
     * Gets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return synonyms of a given uid as String
     * @throws Exception if something goes wrong
     */
    public Map<String, String[]> getSynonymsSettings() throws Exception {
        return this.settingsHandler.getSynonymsSettings(this.uid);
    }

    /**
     * Updates the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param synonyms key (String) value (array) pair of synonyms
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSynonymsSettings(Map<String, String[]> synonyms) throws Exception {
        return this.settingsHandler.updateSynonymsSettings(this.uid, synonyms);
    }

    /**
     * Resets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSynonymsSettings() throws Exception {
        return this.settingsHandler.resetSynonymsSettings(this.uid);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @return stop-words of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getStopWordsSettings() throws Exception {
        return this.settingsHandler.getStopWordsSettings(this.uid);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param stopWords An array of strings that contains the stop-words.
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateStopWordsSettings(String[] stopWords) throws Exception {
        return this.settingsHandler.updateStopWordsSettings(this.uid, stopWords);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetStopWordsSettings() throws Exception {
        return this.settingsHandler.resetStopWordsSettings(this.uid);
    }

    /**
     * Get the searchable attributes of an index.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @return searchable attributes of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getSearchableAttributesSettings() throws Exception {
        return this.settingsHandler.getSearchableAttributesSettings(this.uid);
    }

    /**
     * Updates the searchable attributes an index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param searchableAttributes An array of strings that contains the searchable attributes.
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateSearchableAttributesSettings(String[] searchableAttributes)
            throws Exception {
        return this.settingsHandler.updateSearchableAttributesSettings(
                this.uid, searchableAttributes);
    }

    /**
     * Reset the searchable attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetSearchableAttributesSettings() throws Exception {
        return this.settingsHandler.resetSearchableAttributesSettings(this.uid);
    }

    /**
     * Get the display attributes of an index.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @return display attributes of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getDisplayedAttributesSettings() throws Exception {
        return this.settingsHandler.getDisplayedAttributesSettings(this.uid);
    }

    /**
     * Updates the display attributes of an index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param displayAttributes An array of strings that contains attributes of an index to display
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateDisplayedAttributesSettings(String[] displayAttributes)
            throws Exception {
        return this.settingsHandler.updateDisplayedAttributesSettings(this.uid, displayAttributes);
    }

    /**
     * Reset the displayed attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetDisplayedAttributesSettings() throws Exception {
        return this.settingsHandler.resetDisplayedAttributesSettings(this.uid);
    }

    /**
     * Get an index's filterable attributes.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @return filterable attributes of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String[] getFilterableAttributesSettings() throws Exception {
        return this.settingsHandler.getFilterableAttributesSettings(this.uid);
    }

    /**
     * Update an index's filterable attributes list. This will re-index all documents in the index.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#update-filterable-attributes
     *
     * @param filterableAttributes An array of strings containing the attributes that can be used as
     *     filters at query time.
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateFilterableAttributesSettings(String[] filterableAttributes)
            throws Exception {
        return this.settingsHandler.updateFilterableAttributesSettings(
                this.uid, filterableAttributes);
    }

    /**
     * Reset an index's filterable attributes list back to its default value.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetFilterableAttributesSettings() throws Exception {
        return this.settingsHandler.resetFilterableAttributesSettings(this.uid);
    }

    /**
     * Get the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @return distinct attribute field of a given uid as String
     * @throws Exception if something goes wrong
     */
    public String getDistinctAttributeSettings() throws Exception {
        return this.settingsHandler.getDistinctAttributeSettings(this.uid);
    }

    /**
     * Update the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param distinctAttribute A String: the field name.
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus updateDistinctAttributeSettings(String distinctAttribute) throws Exception {
        return this.settingsHandler.updateDistinctAttributeSettings(this.uid, distinctAttribute);
    }

    /**
     * Reset the distinct attribute field of an index to its default value.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#reset-distinct-attribute
     *
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus resetDistinctAttributeSettings() throws Exception {
        return this.settingsHandler.resetDistinctAttributeSettings(this.uid);
    }

    /**
     * Gets an index update by its id
     *
     * @param updateId ID of the index update
     * @return UpdateStatus
     * @throws Exception if something goes wrong
     */
    public UpdateStatus getUpdate(int updateId) throws Exception {
        return this.gson.fromJson(this.updates.getUpdate(this.uid, updateId), UpdateStatus.class);
    }

    /**
     * Gets all updates of the index
     *
     * @return list of updates in the index
     * @throws Exception if something goes wrong
     */
    public UpdateStatus[] getUpdates() throws Exception {
        return this.gson.fromJson(this.updates.getUpdates(this.uid), UpdateStatus[].class);
    }

    /**
     * Waits for a pending update to be processed
     *
     * @param updateId ID of the index update
     * @throws Exception if timeout is reached
     */
    public void waitForPendingUpdate(int updateId) throws Exception {
        this.waitForPendingUpdate(updateId, 5000, 50);
    }

    /**
     * Waits for a pending update to be processed
     *
     * @param updateId ID of the index update
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws Exception if timeout is reached
     */
    public void waitForPendingUpdate(int updateId, int timeoutInMs, int intervalInMs)
            throws Exception {
        UpdateStatus updateStatus;
        String status = "";
        long startTime = new Date().getTime();
        long elapsedTime = 0;

        while (!status.equals("processed")) {
            if (elapsedTime >= timeoutInMs) {
                throw new Exception();
            }
            updateStatus = this.getUpdate(updateId);
            status = updateStatus.getStatus();
            Thread.sleep(intervalInMs);
            elapsedTime = new Date().getTime() - startTime;
        }
    }

    /**
     * Fetches the primary key of the index in the MeiliSearch instance
     *
     * @throws Exception if something goes wrong
     */
    public void fetchPrimaryKey() throws Exception {
        String requestQuery = "/indexes/" + this.uid;
        MeiliSearchHttpRequest meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
        Index retrievedIndex =
                new Gson().fromJson(meilisearchHttpRequest.get(requestQuery), Index.class);
        this.primaryKey = retrievedIndex.getPrimaryKey();
    }

    /**
     * Add Documents from NDJSON file
     *
     * @param document Document to add in NDJSON string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsNDJSON(String document, String primaryKey) throws Exception {
        return this.documents.addDocuments(
                this.uid,
                document,
                primaryKey,
                Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "application/x-ndjson"));
    }

    /**
     * Add Documents from NDJSON file in Batches
     *
     * @param document Document to add in NDJSON string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsNDJSONinBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {
        String[] documents = document.split("\n");
        StringBuffer subDocuments = new StringBuffer();
        JSONArray arrayResponses = new JSONArray();

        for (int i = 0; i < documents.length; i += batchSize) {
            for (int j = 0; j < batchSize && j + i < documents.length; j++) {
                subDocuments.append(documents[i + j]);
                subDocuments.append("\n");
            }

            arrayResponses.put(
                    new JSONObject(
                            this.documents.addDocuments(
                                    this.uid,
                                    subDocuments.toString(),
                                    primaryKey,
                                    Collections.singletonMap(
                                            HttpHeaders.CONTENT_TYPE, "application/x-ndjson"))));
            subDocuments.setLength(0);
        }
        return arrayResponses.toString();
    }

    /**
     * Add Documents from NDJSON file in Batches
     *
     * @param document Document to add in NDJSON string format
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsNDJSONinBatches(String document) throws Exception {
        return addDocumentsNDJSONinBatches(document, 1000, null);
    }

    /**
     * Add Documents from CSV File
     *
     * @param document Document to add in CSV string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsCSV(String document, String primaryKey) throws Exception {
        return this.documents.addDocuments(
                this.uid,
                document,
                primaryKey,
                Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "text/csv"));
    }

    /**
     * Add CSV Documents in Batches
     *
     * @param document Document to add in CSV string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsCSVinBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {
        String[] documents = document.split("\n");
        StringBuffer subDocuments = new StringBuffer();
        JSONArray arrayResponses = new JSONArray();
        String fields = documents[0];

        for (int i = 1; i < documents.length; i += (batchSize)) {
            subDocuments.append(fields);
            subDocuments.append("\n");
            for (int j = 0; j < (batchSize) && j + i < documents.length; j++) {
                subDocuments.append(documents[i + j]);
                subDocuments.append("\n");
            }

            arrayResponses.put(
                    new JSONObject(
                            this.documents.addDocuments(
                                    this.uid,
                                    subDocuments.toString(),
                                    primaryKey,
                                    Collections.singletonMap(
                                            HttpHeaders.CONTENT_TYPE, "text/csv"))));
            subDocuments.setLength(0);
        }
        return arrayResponses.toString();
    }

    /**
     * Add CSV Documents in Batches
     *
     * @param document Document to add in CSV string format
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String addDocumentsCSVinBatches(String document) throws Exception {
        return addDocumentsCSVinBatches(document, 1000, null);
    }

    /**
     * Update Documents from NDJSON
     *
     * @param document Document to add in NDJSON string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsNDJSON(String document, String primaryKey) throws Exception {
        return this.documents.updateDocuments(
                this.uid,
                document,
                primaryKey,
                Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "application/x-ndjson"));
    }

    /**
     * Update Documents from CSV file
     *
     * @param document Document to add in CSV string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsCSV(String document, String primaryKey) throws Exception {
        return this.documents.updateDocuments(
                this.uid,
                document,
                primaryKey,
                Collections.singletonMap(HttpHeaders.CONTENT_TYPE, "text/csv"));
    }

    /**
     * Update Documents from NDJSON files in Batches
     *
     * @param document Document to add in CSV string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsNDJSONinBatches(
            String document, Integer batchSize, String primaryKey) throws Exception {
        String[] documents = document.split("\n");
        StringBuffer subDocuments = new StringBuffer();
        JSONArray arrayResponses = new JSONArray();

        for (int i = 0; i < documents.length; i += batchSize) {
            for (int j = 0; j < batchSize && j + i < documents.length; j++) {
                subDocuments.append(documents[i + j]);
                subDocuments.append("\n");
            }

            arrayResponses.put(
                    new JSONObject(
                            this.documents.addDocuments(
                                    this.uid,
                                    subDocuments.toString(),
                                    primaryKey,
                                    Collections.singletonMap(
                                            HttpHeaders.CONTENT_TYPE, "application/x-ndjson"))));
            subDocuments.setLength(0);
        }
        return arrayResponses.toString();
    }

    /**
     * Update Documents from NDJSON file in Batches
     *
     * @param document Document to add in CSV string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsNDJSONinBatches(String document, String primaryKey)
            throws Exception {
        return updateDocumentsNDJSONinBatches(document, 1000, primaryKey);
    }

    /**
     * Update Documents from CSV file in Batches
     *
     * @param document Document to add in CSV string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsCSVinBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {
        String[] documents = document.split("\n");
        StringBuffer subDocuments = new StringBuffer();
        JSONArray arrayResponses = new JSONArray();
        String fields = documents[0];

        for (int i = 1; i < documents.length; i += (batchSize)) {
            subDocuments.append(fields);
            subDocuments.append("\n");
            for (int j = 0; j < (batchSize) && j + i < documents.length; j++) {
                subDocuments.append(documents[i + j]);
                subDocuments.append("\n");
            }

            arrayResponses.put(
                    new JSONObject(
                            this.documents.addDocuments(
                                    this.uid,
                                    subDocuments.toString(),
                                    primaryKey,
                                    Collections.singletonMap(
                                            HttpHeaders.CONTENT_TYPE, "text/csv"))));
            subDocuments.setLength(0);
        }
        return arrayResponses.toString();
    }

    /**
     * Update Documents from CSV file in Batches
     *
     * @param document Document to add in CSV string format
     * @param primaryKey PrimaryKey of the Document to Add
     * @return Multiple MeiliSearch API response
     * @throws Exception if something goes wrong
     */
    public String updateDocumentsCSVinBatches(String document, String primaryKey) throws Exception {
        return updateDocumentsCSVinBatches(document, 1000, primaryKey);
    }
}
