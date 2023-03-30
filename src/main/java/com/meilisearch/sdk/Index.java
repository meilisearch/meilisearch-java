package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.*;
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
    @Getter protected String uid;
    @Getter protected String primaryKey;
    @Getter protected String createdAt;
    @Getter protected Pagination pagination;
    @Getter protected Faceting faceting;
    @Getter @ToString.Exclude protected String updatedAt;
    @Getter @ToString.Exclude protected Config config;
    @ToString.Exclude protected Documents documents;
    @ToString.Exclude protected TasksHandler tasksHandler;
    @ToString.Exclude protected Search search;
    @ToString.Exclude protected SettingsHandler settingsHandler;
    @ToString.Exclude protected InstanceHandler instanceHandler;

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
        this.instanceHandler = new InstanceHandler(config);
    }

    /**
     * Gets a documents with the specified uid Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-one-document
     *
     * @param <T> Type of documents returned
     * @param identifier Identifier of the document to get
     * @param targetClass Class of the document returned
     * @return Object containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public <T> T getDocument(String identifier, Class<T> targetClass) throws MeilisearchException {
        return this.documents.<T>getDocument(this.uid, identifier, targetClass);
    }

    /**
     * Gets a documents with the specified uid Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-one-document
     *
     * @param <T> Type of documents returned
     * @param identifier Identifier of the document to get
     * @param param accepted by the get document route
     * @param targetClass Class of documents returned
     * @return Object containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public <T> T getDocument(String identifier, DocumentQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return this.documents.<T>getDocument(this.uid, identifier, param, targetClass);
    }

    /**
     * Gets a documents with the specified uid Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-one-document
     *
     * @param identifier Identifier of the document to get
     * @return String containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public String getRawDocument(String identifier) throws MeilisearchException {
        return this.documents.getRawDocument(this.uid, identifier);
    }

    /**
     * Gets a document with the specified uid and parameters
     * https://docs.meilisearch.com/reference/api/documents.html#get-one-document
     *
     * @param identifier Identifier of the document to get
     * @param param accept by the documents route
     * @return String containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public String getRawDocument(String identifier, DocumentQuery param)
            throws MeilisearchException {
        return this.documents.getRawDocument(this.uid, identifier, param);
    }

    /**
     * Gets documents at the specified index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-documents
     *
     * @param <T> Type of documents returned
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public <T> Results<T> getDocuments(Class<T> targetClass) throws MeilisearchException {
        return this.documents.getDocuments(this.uid, targetClass);
    }

    /**
     * Gets documents at the specified index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-documents
     *
     * @param <T> Type of documents returned
     * @param param accept by the documents route
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if an error occurs
     */
    public <T> Results<T> getDocuments(DocumentsQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return this.documents.getDocuments(this.uid, param, targetClass);
    }

    /**
     * Gets documents as String at the specified index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-documents
     *
     * @return String containing a list of documents
     * @throws MeilisearchException if an error occurs
     */
    public String getRawDocuments() throws MeilisearchException {
        return this.documents.getRawDocuments(this.uid);
    }

    /**
     * Gets documents as String at the specified index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#get-documents
     *
     * @param param accept by the documents route
     * @return String containing a list of documents
     * @throws MeilisearchException if an error occurs
     */
    public String getRawDocuments(DocumentsQuery param) throws MeilisearchException {
        return this.documents.getRawDocuments(this.uid, param);
    }

    /**
     * Adds/Replaces documents in the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-replace-documents
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo addDocuments(String document) throws MeilisearchException {
        return this.documents.addDocuments(this.uid, document, null);
    }

    /**
     * Adds/Replaces documents in the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-replace-documents
     *
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo addDocuments(String document, String primaryKey) throws MeilisearchException {
        return this.documents.addDocuments(this.uid, document, primaryKey);
    }

    /**
     * Adds/Replaces documents in the index in batches Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-replace-documents
     *
     * @param batchSize size of the batch of documents
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo[] addDocumentsInBatches(String document, Integer batchSize, String primaryKey)
            throws MeilisearchException {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        List<TaskInfo> arrayResponses = new ArrayList<TaskInfo>();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.add(
                    this.documents.addDocuments(this.uid, jsonSubArray.toString(), primaryKey));
        }
        return arrayResponses.toArray(new TaskInfo[arrayResponses.size()]);
    }

    /**
     * Adds/Replaces documents in index in batches Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-replace-documents
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo[] addDocumentsInBatches(String document) throws MeilisearchException {
        return this.addDocumentsInBatches(document, 1000, null);
    }

    /**
     * Updates documents in the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-update-documents
     *
     * @param document Document to update in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateDocuments(String document) throws MeilisearchException {
        return this.documents.updateDocuments(this.uid, document, null);
    }

    /**
     * Updates documents in the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-update-documents
     *
     * @param document Document to update in JSON string format
     * @param primaryKey PrimaryKey of the document
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateDocuments(String document, String primaryKey)
            throws MeilisearchException {
        return this.documents.updateDocuments(this.uid, document, primaryKey);
    }

    /**
     * Updates documents in index in batches Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-update-documents
     *
     * @param document Document to add in JSON string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo[] updateDocumentsInBatches(
            String document, Integer batchSize, String primaryKey) throws MeilisearchException {

        JSONArray jsonDocumentsArray = new JSONArray(document);
        JSONArray jsonSubArray = new JSONArray();
        List<TaskInfo> arrayResponses = new ArrayList<TaskInfo>();

        batchSize =
                jsonDocumentsArray.length() < batchSize ? jsonDocumentsArray.length() : batchSize;

        for (int i = 0; i < jsonDocumentsArray.length(); i += batchSize) {
            for (int j = 0; j < batchSize && j + i < jsonDocumentsArray.length(); j++) {
                jsonSubArray.put(j, jsonDocumentsArray.get(i + j));
            }
            arrayResponses.add(
                    this.documents.updateDocuments(this.uid, jsonSubArray.toString(), primaryKey));
        }
        return arrayResponses.toArray(new TaskInfo[arrayResponses.size()]);
    }

    /**
     * Updates documents in index in batches Refer
     * https://docs.meilisearch.com/reference/api/documents.html#add-or-update-documents
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo[] updateDocumentsInBatches(String document) throws MeilisearchException {
        return this.updateDocumentsInBatches(document, 1000, null);
    }

    /**
     * Deletes a document from the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#delete-one-document
     *
     * @param identifier Identifier of the document to delete
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo deleteDocument(String identifier) throws MeilisearchException {
        return this.documents.deleteDocument(this.uid, identifier);
    }

    /**
     * Deletes list of documents from the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#delete-documents-by-batch
     *
     * @param documentsIdentifiers list of identifiers of documents to delete
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo deleteDocuments(List<String> documentsIdentifiers) throws MeilisearchException {
        return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
    }

    /**
     * Deletes all documents in the index Refer
     * https://docs.meilisearch.com/reference/api/documents.html#delete-all-documents
     *
     * @return List of tasks Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo deleteAllDocuments() throws MeilisearchException {
        return this.documents.deleteAllDocuments(this.uid);
    }

    /**
     * Searches documents in the index Refer
     * https://docs.meilisearch.com/reference/api/search.html#search-in-an-index-with-post-route
     *
     * @param q Query string
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public SearchResult search(String q) throws MeilisearchException {
        return this.search.search(this.uid, q);
    }

    /**
     * Searches documents in the index Refer
     * https://docs.meilisearch.com/reference/api/search.html#search-in-an-index-with-post-route
     *
     * @param searchRequest SearchRequest SearchRequest
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public Searchable search(SearchRequest searchRequest) throws MeilisearchException {
        return this.search.search(this.uid, searchRequest);
    }

    public String rawSearch(String query) throws MeilisearchException {
        return this.search.rawSearch(this.uid, query);
    }

    public String rawSearch(SearchRequest searchRequest) throws MeilisearchException {
        return this.search.rawSearch(this.uid, searchRequest);
    }

    /**
     * Gets the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return settings of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public Settings getSettings() throws MeilisearchException {
        return this.settingsHandler.getSettings(this.uid);
    }

    /**
     * Updates the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param settings the object that contains the data with the new settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateSettings(Settings settings) throws MeilisearchException {
        return this.settingsHandler.updateSettings(this.uid, settings);
    }

    /**
     * Resets the settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetSettings() throws MeilisearchException {
        return this.settingsHandler.resetSettings(this.uid);
    }

    /**
     * Gets the ranking rules settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-settings
     *
     * @return ranking rules of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getRankingRulesSettings() throws MeilisearchException {
        return this.settingsHandler.getRankingRulesSettings(this.uid);
    }

    /**
     * Updates the ranking rules settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-settings
     *
     * @param rankingRules array that contain the data with the new ranking rules
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateRankingRulesSettings(String[] rankingRules) throws MeilisearchException {
        return this.settingsHandler.updateRankingRulesSettings(this.uid, rankingRules);
    }

    /**
     * Resets the ranking rules settings of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-settings
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetRankingRulesSettings() throws MeilisearchException {
        return this.settingsHandler.resetRankingRulesSettings(this.uid);
    }

    /**
     * Gets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#get-synonyms
     *
     * @return synonyms of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public Map<String, String[]> getSynonymsSettings() throws MeilisearchException {
        return this.settingsHandler.getSynonymsSettings(this.uid);
    }

    /**
     * Updates the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#update-synonyms
     *
     * @param synonyms key (String) value (array) pair of synonyms
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateSynonymsSettings(Map<String, String[]> synonyms)
            throws MeilisearchException {
        return this.settingsHandler.updateSynonymsSettings(this.uid, synonyms);
    }

    /**
     * Resets the synonyms settings of the index Refer
     * https://docs.meilisearch.com/reference/api/synonyms.html#reset-synonyms
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetSynonymsSettings() throws MeilisearchException {
        return this.settingsHandler.resetSynonymsSettings(this.uid);
    }

    /**
     * Gets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#get-stop-words
     *
     * @return stop-words of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getStopWordsSettings() throws MeilisearchException {
        return this.settingsHandler.getStopWordsSettings(this.uid);
    }

    /**
     * Updates the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#update-stop-words
     *
     * @param stopWords An array of strings that contains the stop-words.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateStopWordsSettings(String[] stopWords) throws MeilisearchException {
        return this.settingsHandler.updateStopWordsSettings(this.uid, stopWords);
    }

    /**
     * Resets the stop-words settings of the index Refer
     * https://docs.meilisearch.com/reference/api/stop_words.html#reset-stop-words
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetStopWordsSettings() throws MeilisearchException {
        return this.settingsHandler.resetStopWordsSettings(this.uid);
    }

    /**
     * Gets the searchable attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#get-searchable-attributes
     *
     * @return searchable attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getSearchableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getSearchableAttributesSettings(this.uid);
    }

    /**
     * Updates the searchable attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#update-searchable-attributes
     *
     * @param searchableAttributes An array of strings that contains the searchable attributes.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateSearchableAttributesSettings(String[] searchableAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateSearchableAttributesSettings(
                this.uid, searchableAttributes);
    }

    /**
     * Resets the searchable attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/searchable_attributes.html#reset-searchable-attributes
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetSearchableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetSearchableAttributesSettings(this.uid);
    }

    /**
     * Gets the display attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#get-displayed-attributes
     *
     * @return display attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getDisplayedAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getDisplayedAttributesSettings(this.uid);
    }

    /**
     * Updates the display attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#update-displayed-attributes
     *
     * @param displayAttributes An array of strings that contains attributes of an index to display
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateDisplayedAttributesSettings(String[] displayAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateDisplayedAttributesSettings(this.uid, displayAttributes);
    }

    /**
     * Resets the displayed attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/displayed_attributes.html#reset-displayed-attributes
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetDisplayedAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetDisplayedAttributesSettings(this.uid);
    }

    /**
     * Gets the filterable attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#get-filterable-attributes
     *
     * @return filterable attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String[] getFilterableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getFilterableAttributesSettings(this.uid);
    }

    /**
     * Updates the filterable attributes of the index. This will re-index all documents in the index
     * Refer
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#update-filterable-attributes
     *
     * @param filterableAttributes An array of strings containing the attributes that can be used as
     *     filters at query time.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateFilterableAttributesSettings(String[] filterableAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateFilterableAttributesSettings(
                this.uid, filterableAttributes);
    }

    /**
     * Resets the filterable attributes of the index Refer
     * https://docs.meilisearch.com/reference/api/filterable_attributes.html#reset-filterable-attributes
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetFilterableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetFilterableAttributesSettings(this.uid);
    }

    public String[] getSortableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getSortableAttributesSettings(this.uid);
    }

    public TaskInfo updateSortableAttributesSettings(String[] sortableAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateSortableAttributesSettings(this.uid, sortableAttributes);
    }

    public TaskInfo resetSortableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetSortableAttributesSettings(this.uid);
    }

    /**
     * Gets the distinct attribute field of the index Refer
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#get-distinct-attribute
     *
     * @return distinct attribute field of a given uid as String
     * @throws MeilisearchException if an error occurs
     */
    public String getDistinctAttributeSettings() throws MeilisearchException {
        return this.settingsHandler.getDistinctAttributeSettings(this.uid);
    }

    /**
     * Updates the distinct attribute field of the index Refer
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#update-distinct-attribute
     *
     * @param distinctAttribute A String: the field name.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateDistinctAttributeSettings(String distinctAttribute)
            throws MeilisearchException {
        return this.settingsHandler.updateDistinctAttributeSettings(this.uid, distinctAttribute);
    }

    /**
     * Resets the distinct attribute field of the index Refer
     * https://docs.meilisearch.com/reference/api/distinct_attribute.html#reset-distinct-attribute
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetDistinctAttributeSettings() throws MeilisearchException {
        return this.settingsHandler.resetDistinctAttributeSettings(this.uid);
    }

    /**
     * Gets the typo tolerance field of the index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#get-typo-tolerance
     *
     * @return TypoTolerance instance from Index
     * @throws MeilisearchException if an error occurs
     */
    public TypoTolerance getTypoToleranceSettings() throws MeilisearchException {
        return this.settingsHandler.getTypoToleranceSettings(this.uid);
    }

    /**
     * Updates the typo tolerance field of the index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#update-typo-tolerance
     *
     * @param typoTolerance A TypoTolerance instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateTypoToleranceSettings(TypoTolerance typoTolerance)
            throws MeilisearchException {
        return this.settingsHandler.updateTypoToleranceSettings(this.uid, typoTolerance);
    }

    /**
     * Resets the typo tolerance field of the index Refer
     * https://docs.meilisearch.com/reference/api/typo_tolerance.html#reset-typo-tolerance
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetTypoToleranceSettings() throws MeilisearchException {
        return this.settingsHandler.resetTypoToleranceSettings(this.uid);
    }

    /**
     * Gets the pagination field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-pagination-settings
     *
     * @return Pagination instance from Index
     * @throws MeilisearchException if an error occurs
     */
    public Pagination getPaginationSettings() throws MeilisearchException {
        return this.settingsHandler.getPaginationSettings(this.uid);
    }

    /**
     * Updates the pagination field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-pagination-settings
     *
     * @param pagination A pagination instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updatePaginationSettings(Pagination pagination) throws MeilisearchException {
        return this.settingsHandler.updatePaginationSettings(this.uid, pagination);
    }

    /**
     * Resets the pagination field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-pagination-settings
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetPaginationSettings() throws MeilisearchException {
        return this.settingsHandler.resetPaginationSettings(this.uid);
    }

    /**
     * Gets the faceting field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#get-faceting-settings
     *
     * @return Faceting instance from Index
     * @throws MeilisearchException if an error occurs
     */
    public Faceting getFacetingSettings() throws MeilisearchException {
        return this.settingsHandler.getFacetingSettings(this.uid);
    }

    /**
     * Updates the faceting field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#update-faceting-settings
     *
     * @param faceting A faceting instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo updateFacetingSettings(Faceting faceting) throws MeilisearchException {
        return this.settingsHandler.updateFacetingSettings(this.uid, faceting);
    }

    /**
     * Resets the faceting field of the index Refer
     * https://docs.meilisearch.com/reference/api/settings.html#reset-faceting-settings
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     */
    public TaskInfo resetFacetingSettings() throws MeilisearchException {
        return this.settingsHandler.resetFacetingSettings(this.uid);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database Refer
     * https://docs.meilisearch.com/reference/api/stats.html#stats-object
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    public IndexStats getStats() throws MeilisearchException {
        return this.instanceHandler.getIndexStats(this.uid);
    }

    /**
     * Retrieves an index tasks by its uid Refer
     * https://docs.meilisearch.com/reference/api/tasks.html#get-one-task
     *
     * @param taskId Identifier of the requested index task
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     */
    public Task getTask(int taskId) throws MeilisearchException {
        return this.tasksHandler.getTask(taskId);
    }

    /**
     * Retrieves list of tasks of the index Refer
     * https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @return List of tasks in the Meilisearch index
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks(this.uid);
    }

    /**
     * Retrieves list of tasks of the index Refer
     * https://docs.meilisearch.com/reference/api/tasks.html#get-tasks
     *
     * @param param accept by the tasks route
     * @return List of tasks in the Meilisearch index
     * @throws MeilisearchException if an error occurs
     */
    public TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        return this.tasksHandler.getTasks(this.uid, param);
    }

    /**
     * Waits for a task to be processed Refer
     * https://docs.meilisearch.com/reference/api/tasks.html#task-object
     *
     * @param taskId Identifier of the requested Task
     * @throws MeilisearchException if an error occurs or if timeout is reached
     */
    public void waitForTask(int taskId) throws MeilisearchException {
        this.tasksHandler.waitForTask(taskId, 5000, 50);
    }

    /**
     * Waits for a task to be processed Refer
     * https://docs.meilisearch.com/reference/api/tasks.html#task-object
     *
     * @param taskId ID of the index update
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws MeilisearchException if an error occurs or if timeout is reached
     */
    public void waitForTask(int taskId, int timeoutInMs, int intervalInMs)
            throws MeilisearchException {
        this.tasksHandler.waitForTask(taskId, timeoutInMs, intervalInMs);
    }

    /**
     * Fetches the primary key of the index in the Meilisearch instance
     *
     * @throws MeilisearchException if an error occurs
     */
    public void fetchPrimaryKey() throws MeilisearchException {
        String requestQuery = "/indexes/" + this.uid;
        HttpClient httpClient = config.httpClient;
        Index retrievedIndex = httpClient.get(requestQuery, Index.class);
        this.primaryKey = retrievedIndex.getPrimaryKey();
    }
}
