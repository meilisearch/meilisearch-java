package com.meilisearch.sdk;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.meilisearch.sdk.model.SearchResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONArray;

/** Meilisearch index */
@ToString
public class Index implements Serializable {
    @Getter String uid;

    @Getter String primaryKey;

    @Getter String createdAt;

    @Getter @ToString.Exclude String updatedAt;

    @Getter @ToString.Exclude Config config;

    @ToString.Exclude Documents documents;

    @ToString.Exclude TasksHandler tasksHandler;

    @ToString.Exclude Search search;

    @ToString.Exclude SettingsHandler settingsHandler;

    Gson gson = new Gson();

    /**
     * Sets the Meilisearch configuration for the index
     *
     * @param config Meilisearch configuration to use
     */
    void setConfig(Config config) {
        this.config = config;
        this.documents = new Documents(config);
        this.tasksHandler = new TasksHandler(config);
        this.search = new Search(config);
        this.settingsHandler = new SettingsHandler(config);
    }

    /**
     * Gets a document in the index
     *
     * @param identifier Identifier of the document to get
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public String getDocument(String identifier) throws Exception {
        return this.documents.getDocument(this.uid, identifier);
    }

    /**
     * Gets documents in the index
     *
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public String getDocuments() throws Exception {
        return this.documents.getDocuments(this.uid);
    }

    /**
     * Gets documents in the index and limit the number of documents returned
     *
     * @param limits Maximum amount of documents to return
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public String getDocuments(int limits) throws Exception {
        return this.documents.getDocuments(this.uid, limits);
    }

    /**
     * Gets documents in the index and limit the number of documents returned
     *
     * @param limits Maximum amount of documents to return
     * @param offset Number of documents to skip
     * @return Meilisearch API response
     * @throws Exception if an error occurs
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
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public String getDocuments(int limits, int offset, List<String> attributesToRetrieve)
            throws Exception {
        return this.documents.getDocuments(this.uid, limits, offset, attributesToRetrieve);
    }

    /**
     * Adds a document in the index
     *
     * @param document Document to add in JSON string format
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task addDocuments(String document) throws Exception {
        return this.documents.addDocuments(this.uid, document, null);
    }

    /**
     * Adds a document in the index
     *
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task addDocuments(String document, String primaryKey) throws Exception {
        return this.documents.addDocuments(this.uid, document, primaryKey);
    }

    /**
     * Adds a document in the index in batches
     *
     * @param batchSize size of the batch of documents
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task[] addDocumentsInBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        List<Task> arrayResponses = new ArrayList<Task>();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.add(
                    this.documents.addDocuments(this.uid, jsonSubArray.toString(), primaryKey));
        }
        return arrayResponses.toArray(new Task[arrayResponses.size()]);
    }

    /**
     * Add Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task[] addDocumentsInBatches(String document) throws Exception {
        return this.addDocumentsInBatches(document, 1000, null);
    }

    /**
     * Updates a document in the index
     *
     * @param document Document to update in JSON string format
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task updateDocuments(String document) throws Exception {
        return this.documents.updateDocuments(this.uid, document, null);
    }

    /**
     * Updates a document in the index
     *
     * @param document Document to update in JSON string format
     * @param primaryKey PrimaryKey of the document
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task updateDocuments(String document, String primaryKey) throws Exception {
        return this.documents.updateDocuments(this.uid, document, primaryKey);
    }

    /**
     * Update Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the document to add
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task[] updateDocumentsInBatches(String document, Integer batchSize, String primaryKey)
            throws Exception {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        List<Task> arrayResponses = new ArrayList<Task>();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.add(
                    this.documents.updateDocuments(this.uid, jsonSubArray.toString(), primaryKey));
        }
        return arrayResponses.toArray(new Task[arrayResponses.size()]);
    }

    /**
     * Update Documents in Index in Batches
     *
     * @param document Document to add in JSON string format
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task[] updateDocumentsInBatches(String document) throws Exception {
        return this.updateDocumentsInBatches(document, 1000, null);
    }

    /**
     * Deletes a document from the index
     *
     * @param identifier Identifier of the document to delete
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task deleteDocument(String identifier) throws Exception {
        return this.documents.deleteDocument(this.uid, identifier);
    }

    /**
     * Deletes list of documents from the index
     *
     * @param documentsIdentifiers list of identifiers of documents to delete
     * @return Task Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task deleteDocuments(List<String> documentsIdentifiers) throws Exception {
        return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
    }

    /**
     * Deletes all documents in the index
     *
     * @return List of tasks Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Task deleteAllDocuments() throws Exception {
        return this.documents.deleteAllDocuments(this.uid);
    }

    /**
     * Searches documents in index
     *
     * @param q Query string
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public SearchResult search(String q) throws Exception {
        return this.search.search(this.uid, q);
    }

    /**
     * Searches documents in index
     *
     * @param searchRequest SearchRequest SearchRequest
     * @return Meilisearch API response
     * @throws Exception if an error occurs
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
     * @throws Exception if an error occurs
     */
    public Settings getSettings() throws Exception {
        return this.settingsHandler.getSettings(this.uid);
    }

    /**
     * Updates the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param settings the object that contains the data with the new settings
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateSettings(Settings settings) throws Exception {
        return this.settingsHandler.updateSettings(this.uid, settings);
    }

    /**
     * Resets the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetSettings() throws Exception {
        return this.settingsHandler.resetSettings(this.uid);
    }

    /**
     * Gets the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return ranking rules of a given uid as String
     * @throws Exception if an error occurs
     */
    public String[] getRankingRuleSettings() throws Exception {
        return this.settingsHandler.getRankingRuleSettings(this.uid);
    }

    /**
     * Updates the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param rankingRules array that contain the data with the new ranking rules
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateRankingRuleSettings(String[] rankingRules) throws Exception {
        return this.settingsHandler.updateRankingRuleSettings(this.uid, rankingRules);
    }

    /**
     * Resets the ranking rule settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetRankingRuleSettings() throws Exception {
        return this.settingsHandler.resetRankingRulesSettings(this.uid);
    }

    /**
     * Gets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return synonyms of a given uid as String
     * @throws Exception if an error occurs
     */
    public Map<String, String[]> getSynonymsSettings() throws Exception {
        return this.settingsHandler.getSynonymsSettings(this.uid);
    }

    /**
     * Updates the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param synonyms key (String) value (array) pair of synonyms
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateSynonymsSettings(Map<String, String[]> synonyms) throws Exception {
        return this.settingsHandler.updateSynonymsSettings(this.uid, synonyms);
    }

    /**
     * Resets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetSynonymsSettings() throws Exception {
        return this.settingsHandler.resetSynonymsSettings(this.uid);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @return stop-words of a given uid as String
     * @throws Exception if an error occurs
     */
    public String[] getStopWordsSettings() throws Exception {
        return this.settingsHandler.getStopWordsSettings(this.uid);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param stopWords An array of strings that contains the stop-words.
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateStopWordsSettings(String[] stopWords) throws Exception {
        return this.settingsHandler.updateStopWordsSettings(this.uid, stopWords);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetStopWordsSettings() throws Exception {
        return this.settingsHandler.resetStopWordsSettings(this.uid);
    }

    /**
     * Get the searchable attributes of an index.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @return searchable attributes of a given uid as String
     * @throws Exception if an error occurs
     */
    public String[] getSearchableAttributesSettings() throws Exception {
        return this.settingsHandler.getSearchableAttributesSettings(this.uid);
    }

    /**
     * Updates the searchable attributes an index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param searchableAttributes An array of strings that contains the searchable attributes.
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateSearchableAttributesSettings(String[] searchableAttributes) throws Exception {
        return this.settingsHandler.updateSearchableAttributesSettings(
                this.uid, searchableAttributes);
    }

    /**
     * Reset the searchable attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetSearchableAttributesSettings() throws Exception {
        return this.settingsHandler.resetSearchableAttributesSettings(this.uid);
    }

    /**
     * Get the display attributes of an index.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @return display attributes of a given uid as String
     * @throws Exception if an error occurs
     */
    public String[] getDisplayedAttributesSettings() throws Exception {
        return this.settingsHandler.getDisplayedAttributesSettings(this.uid);
    }

    /**
     * Updates the display attributes of an index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param displayAttributes An array of strings that contains attributes of an index to display
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateDisplayedAttributesSettings(String[] displayAttributes) throws Exception {
        return this.settingsHandler.updateDisplayedAttributesSettings(this.uid, displayAttributes);
    }

    /**
     * Reset the displayed attributes of the index to the default value.
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetDisplayedAttributesSettings() throws Exception {
        return this.settingsHandler.resetDisplayedAttributesSettings(this.uid);
    }

    /**
     * Get an index's filterable attributes.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @return filterable attributes of a given uid as String
     * @throws Exception if an error occurs
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
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateFilterableAttributesSettings(String[] filterableAttributes) throws Exception {
        return this.settingsHandler.updateFilterableAttributesSettings(
                this.uid, filterableAttributes);
    }

    /**
     * Reset an index's filterable attributes list back to its default value.
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetFilterableAttributesSettings() throws Exception {
        return this.settingsHandler.resetFilterableAttributesSettings(this.uid);
    }

    /**
     * Get the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @return distinct attribute field of a given uid as String
     * @throws Exception if an error occurs
     */
    public String getDistinctAttributeSettings() throws Exception {
        return this.settingsHandler.getDistinctAttributeSettings(this.uid);
    }

    /**
     * Update the distinct attribute field of an index.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param distinctAttribute A String: the field name.
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateDistinctAttributeSettings(String distinctAttribute) throws Exception {
        return this.settingsHandler.updateDistinctAttributeSettings(this.uid, distinctAttribute);
    }

    /**
     * Reset the distinct attribute field of an index to its default value.
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#reset-distinct-attribute
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetDistinctAttributeSettings() throws Exception {
        return this.settingsHandler.resetDistinctAttributeSettings(this.uid);
    }

    /**
     * Get the typo tolerance field of an index.
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#get-typo-tolerance
     *
     * @return TypoTolerance instance from Index
     * @throws Exception if an error occurs
     */
    public TypoTolerance getTypoToleranceSettings() throws Exception {
        return this.settingsHandler.getTypoToleranceSettings(this.uid);
    }

    /**
     * Update the typo tolerance field of an index.
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#update-typo-tolerance
     *
     * @param typoTolerance A TypoTolerance instance
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task updateTypoToleranceSettings(TypoTolerance typoTolerance) throws Exception {
        return this.settingsHandler.updateTypoToleranceSettings(this.uid, typoTolerance);
    }

    /**
     * Reset the typo tolerance field of an index to its default value.
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#reset-typo-tolerance
     *
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task resetTypoToleranceSettings() throws Exception {
        return this.settingsHandler.resetTypoToleranceSettings(this.uid);
    }

    /**
     * Retrieves an index tasks by its ID
     *
     * @param taskId Identifier of the requested index task
     * @return Task instance
     * @throws Exception if an error occurs
     */
    public Task getTask(int taskId) throws Exception {
        return this.tasksHandler.getTask(this.uid, taskId);
    }

    /**
     * Retrieves list of tasks of the index
     *
     * @return List of tasks in the MeiliSearch index
     * @throws Exception if an error occurs
     */
    public Task[] getTasks() throws Exception {
        return this.tasksHandler.getTasks(this.uid);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskId Identifier of the requested Task
     * @throws Exception if an error occurs or if timeout is reached
     */
    public void waitForTask(int taskId) throws Exception {
        this.tasksHandler.waitForTask(taskId, 5000, 50);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskId ID of the index update
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws Exception if an error occurs or if timeout is reached
     */
    public void waitForTask(int taskId, int timeoutInMs, int intervalInMs) throws Exception {
        this.tasksHandler.waitForTask(taskId, timeoutInMs, intervalInMs);
    }

    /**
     * Fetches the primary key of the index in the Meilisearch instance
     *
     * @throws Exception if an error occurs
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
}
