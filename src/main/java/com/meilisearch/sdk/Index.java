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
    @Getter @ToString.Exclude protected transient Config config;
    @ToString.Exclude protected transient Documents documents;
    @ToString.Exclude protected transient TasksHandler tasksHandler;
    @ToString.Exclude protected transient Search search;
    @ToString.Exclude protected transient SettingsHandler settingsHandler;
    @ToString.Exclude protected transient InstanceHandler instanceHandler;

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
     * Gets a documents with the specified uid
     *
     * @param <T> Type of documents returned
     * @param identifier Identifier of the document to get
     * @param targetClass Class of the document returned
     * @return Object containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-one-document">API
     *     specification</a>
     */
    public <T> T getDocument(String identifier, Class<T> targetClass) throws MeilisearchException {
        return this.documents.<T>getDocument(this.uid, identifier, targetClass);
    }

    /**
     * Gets a documents with the specified uid
     *
     * @param <T> Type of documents returned
     * @param identifier Identifier of the document to get
     * @param param accepted by the get document route
     * @param targetClass Class of documents returned
     * @return Object containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-one-document">API
     *     specification</a>
     */
    public <T> T getDocument(String identifier, DocumentQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return this.documents.<T>getDocument(this.uid, identifier, param, targetClass);
    }

    /**
     * Gets a documents with the specified uid
     *
     * @param identifier Identifier of the document to get
     * @return String containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-one-document">API
     *     specification</a>
     */
    public String getRawDocument(String identifier) throws MeilisearchException {
        return this.documents.getRawDocument(this.uid, identifier);
    }

    /**
     * Gets a document with the specified uid and parameters
     *
     * @param identifier Identifier of the document to get
     * @param param accept by the documents route
     * @return String containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-one-document">API
     *     specification</a>
     */
    public String getRawDocument(String identifier, DocumentQuery param)
            throws MeilisearchException {
        return this.documents.getRawDocument(this.uid, identifier, param);
    }

    /**
     * Gets documents at the specified index
     *
     * @param <T> Type of documents returned
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-documents">API
     *     specification</a>
     */
    public <T> Results<T> getDocuments(Class<T> targetClass) throws MeilisearchException {
        return this.documents.getDocuments(this.uid, targetClass);
    }

    /**
     * Gets documents at the specified index
     *
     * @param <T> Type of documents returned
     * @param param accept by the documents route
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-documents">API
     *     specification</a>
     */
    public <T> Results<T> getDocuments(DocumentsQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return this.documents.getDocuments(this.uid, param, targetClass);
    }

    /**
     * Gets documents as String at the specified index
     *
     * @return String containing a list of documents
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-documents">API
     *     specification</a>
     */
    public String getRawDocuments() throws MeilisearchException {
        return this.documents.getRawDocuments(this.uid);
    }

    /**
     * Gets documents as String at the specified index
     *
     * @param param accept by the documents route
     * @return String containing a list of documents
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/documents#get-documents">API
     *     specification</a>
     */
    public String getRawDocuments(DocumentsQuery param) throws MeilisearchException {
        return this.documents.getRawDocuments(this.uid, param);
    }

    /**
     * Adds/Replaces documents in the index
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo addDocuments(String document) throws MeilisearchException {
        return this.documents.addDocuments(this.uid, document, null, null);
    }

    /**
     * Adds/Replaces documents in the index
     *
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo addDocuments(String document, String primaryKey) throws MeilisearchException {
        return this.documents.addDocuments(this.uid, document, primaryKey, null);
    }

    /**
     * Adds/Replaces documents in the index
     *
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @param csvDelimiter Custom delimiter to use for the document being added
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo addDocuments(String document, String primaryKey, String csvDelimiter) throws MeilisearchException {
        return this.documents.addDocuments(this.uid, document, primaryKey, csvDelimiter);
    }

    /**
     * Adds/Replaces documents in the index in batches
     *
     * @param batchSize size of the batch of documents
     * @param document Document to add in JSON string format
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
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
     * Adds/Replaces documents in index in batches
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo[] addDocumentsInBatches(String document) throws MeilisearchException {
        return this.addDocumentsInBatches(document, 1000, null);
    }

    /**
     * Updates documents in the index
     *
     * @param document Document to update in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo updateDocuments(String document) throws MeilisearchException {
        return this.documents.updateDocuments(this.uid, document, null);
    }

    /**
     * Updates documents in the index
     *
     * @param document Document to update in JSON string format
     * @param primaryKey PrimaryKey of the document
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo updateDocuments(String document, String primaryKey)
            throws MeilisearchException {
        return this.documents.updateDocuments(this.uid, document, primaryKey);
    }

    /**
     * Updates documents in index in batches
     *
     * @param document Document to add in JSON string format
     * @param batchSize size of the batch of documents
     * @param primaryKey PrimaryKey of the document to add
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
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
     * Updates documents in index in batches
     *
     * @param document Document to add in JSON string format
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#add-or-replace-documents">API
     *     specification</a>
     */
    public TaskInfo[] updateDocumentsInBatches(String document) throws MeilisearchException {
        return this.updateDocumentsInBatches(document, 1000, null);
    }

    /**
     * Deletes a document from the index
     *
     * @param identifier Identifier of the document to delete
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#delete-one-document">API
     *     specification</a>
     */
    public TaskInfo deleteDocument(String identifier) throws MeilisearchException {
        return this.documents.deleteDocument(this.uid, identifier);
    }

    /**
     * Deletes list of documents from the index
     *
     * @param documentsIdentifiers list of identifiers of documents to delete
     * @return TaskInfo Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#delete-documents-by-batch">API
     *     specification</a>
     */
    public TaskInfo deleteDocuments(List<String> documentsIdentifiers) throws MeilisearchException {
        return this.documents.deleteDocuments(this.uid, documentsIdentifiers);
    }

    /**
     * Deletes all documents in the index
     *
     * @return List of tasks Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/documents#delete-all-documents">API
     *     specification</a>
     */
    public TaskInfo deleteAllDocuments() throws MeilisearchException {
        return this.documents.deleteAllDocuments(this.uid);
    }

    /**
     * Searches documents in the index
     *
     * @param q Query string
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/search#search-in-an-index-with-post">API
     *     specification</a>
     */
    public SearchResult search(String q) throws MeilisearchException {
        return this.search.search(this.uid, q);
    }

    /**
     * Searches documents in the index
     *
     * @param searchRequest SearchRequest SearchRequest
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/search#search-in-an-index-with-post">API
     *     specification</a>
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
     * Gets the settings of the index
     *
     * @return settings of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-settings">API
     *     specification</a>
     */
    public Settings getSettings() throws MeilisearchException {
        return this.settingsHandler.getSettings(this.uid);
    }

    /**
     * Updates the settings of the index
     *
     * @param settings the object that contains the data with the new settings
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#update-settings">API
     *     specification</a>
     */
    public TaskInfo updateSettings(Settings settings) throws MeilisearchException {
        return this.settingsHandler.updateSettings(this.uid, settings);
    }

    /**
     * Resets the settings of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#reset-settings">API
     *     specification</a>
     */
    public TaskInfo resetSettings() throws MeilisearchException {
        return this.settingsHandler.resetSettings(this.uid);
    }

    /**
     * Gets the ranking rules settings of the index
     *
     * @return ranking rules of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-settings">API
     *     specification</a>
     */
    public String[] getRankingRulesSettings() throws MeilisearchException {
        return this.settingsHandler.getRankingRulesSettings(this.uid);
    }

    /**
     * Updates the ranking rules settings of the index
     *
     * @param rankingRules array that contain the data with the new ranking rules
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#update-settings">API
     *     specification</a>
     */
    public TaskInfo updateRankingRulesSettings(String[] rankingRules) throws MeilisearchException {
        return this.settingsHandler.updateRankingRulesSettings(this.uid, rankingRules);
    }

    /**
     * Resets the ranking rules settings of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#reset-settings">API
     *     specification</a>
     */
    public TaskInfo resetRankingRulesSettings() throws MeilisearchException {
        return this.settingsHandler.resetRankingRulesSettings(this.uid);
    }

    /**
     * Gets the synonyms settings of the index
     *
     * @return synonyms of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-synonyms">API
     *     specification</a>
     */
    public Map<String, String[]> getSynonymsSettings() throws MeilisearchException {
        return this.settingsHandler.getSynonymsSettings(this.uid);
    }

    /**
     * Updates the synonyms settings of the index
     *
     * @param synonyms key (String) value (array) pair of synonyms
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#update-synonyms">API
     *     specification</a>
     */
    public TaskInfo updateSynonymsSettings(Map<String, String[]> synonyms)
            throws MeilisearchException {
        return this.settingsHandler.updateSynonymsSettings(this.uid, synonyms);
    }

    /**
     * Resets the synonyms settings of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#reset-synonyms">API
     *     specification</a>
     */
    public TaskInfo resetSynonymsSettings() throws MeilisearchException {
        return this.settingsHandler.resetSynonymsSettings(this.uid);
    }

    /**
     * Gets the stop-words settings of the index
     *
     * @return stop-words of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-stop-words">API
     *     specification</a>
     */
    public String[] getStopWordsSettings() throws MeilisearchException {
        return this.settingsHandler.getStopWordsSettings(this.uid);
    }

    /**
     * Updates the stop-words settings of the index
     *
     * @param stopWords An array of strings that contains the stop-words.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#update-stop-words">API
     *     specification</a>
     */
    public TaskInfo updateStopWordsSettings(String[] stopWords) throws MeilisearchException {
        return this.settingsHandler.updateStopWordsSettings(this.uid, stopWords);
    }

    /**
     * Resets the stop-words settings of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#reset-stop-words">API
     *     specification</a>
     */
    public TaskInfo resetStopWordsSettings() throws MeilisearchException {
        return this.settingsHandler.resetStopWordsSettings(this.uid);
    }

    /**
     * Gets the searchable attributes of the index
     *
     * @return searchable attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-searchable-attributes">API
     *     specification</a>
     */
    public String[] getSearchableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getSearchableAttributesSettings(this.uid);
    }

    /**
     * Updates the searchable attributes of the index
     *
     * @param searchableAttributes An array of strings that contains the searchable attributes.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-searchable-attributes">API
     *     specification</a>
     */
    public TaskInfo updateSearchableAttributesSettings(String[] searchableAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateSearchableAttributesSettings(
                this.uid, searchableAttributes);
    }

    /**
     * Resets the searchable attributes of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-searchable-attributes">API
     *     specification</a>
     */
    public TaskInfo resetSearchableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetSearchableAttributesSettings(this.uid);
    }

    /**
     * Gets the display attributes of the index
     *
     * @return display attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-displayed-attributes">API
     *     specification</a>
     */
    public String[] getDisplayedAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getDisplayedAttributesSettings(this.uid);
    }

    /**
     * Updates the display attributes of the index
     *
     * @param displayAttributes An array of strings that contains attributes of an index to display
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-displayed-attributes">API
     *     specification</a>
     */
    public TaskInfo updateDisplayedAttributesSettings(String[] displayAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateDisplayedAttributesSettings(this.uid, displayAttributes);
    }

    /**
     * Resets the displayed attributes of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-displayed-attributes">API
     *     specification</a>
     */
    public TaskInfo resetDisplayedAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.resetDisplayedAttributesSettings(this.uid);
    }

    /**
     * Gets the filterable attributes of the index
     *
     * @return filterable attributes of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-filterable-attributes">API
     *     specification</a>
     */
    public String[] getFilterableAttributesSettings() throws MeilisearchException {
        return this.settingsHandler.getFilterableAttributesSettings(this.uid);
    }

    /**
     * Updates the filterable attributes of the index. This will re-index all documents in the index
     *
     * @param filterableAttributes An array of strings containing the attributes that can be used as
     *     filters at query time.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-filterable-attributes">API
     *     specification</a>
     */
    public TaskInfo updateFilterableAttributesSettings(String[] filterableAttributes)
            throws MeilisearchException {
        return this.settingsHandler.updateFilterableAttributesSettings(
                this.uid, filterableAttributes);
    }

    /**
     * Resets the filterable attributes of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-filterable-attributes">API
     *     specification</a>
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
     * Gets the distinct attribute field of the index
     *
     * @return distinct attribute field of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-distinct-attribute">API
     *     specification</a>
     */
    public String getDistinctAttributeSettings() throws MeilisearchException {
        return this.settingsHandler.getDistinctAttributeSettings(this.uid);
    }

    /**
     * Updates the distinct attribute field of the index
     *
     * @param distinctAttribute A String: the field name.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-distinct-attribute">API
     *     specification</a>
     */
    public TaskInfo updateDistinctAttributeSettings(String distinctAttribute)
            throws MeilisearchException {
        return this.settingsHandler.updateDistinctAttributeSettings(this.uid, distinctAttribute);
    }

    /**
     * Resets the distinct attribute field of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-distinct-attribute">API
     *     specification</a>
     */
    public TaskInfo resetDistinctAttributeSettings() throws MeilisearchException {
        return this.settingsHandler.resetDistinctAttributeSettings(this.uid);
    }

    /**
     * Gets the typo tolerance field of the index
     *
     * @return TypoTolerance instance from Index
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-typo-tolerance">API
     *     specification</a>
     */
    public TypoTolerance getTypoToleranceSettings() throws MeilisearchException {
        return this.settingsHandler.getTypoToleranceSettings(this.uid);
    }

    /**
     * Updates the typo tolerance field of the index
     *
     * @param typoTolerance A TypoTolerance instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-typo-tolerance">API
     *     specification</a>
     */
    public TaskInfo updateTypoToleranceSettings(TypoTolerance typoTolerance)
            throws MeilisearchException {
        return this.settingsHandler.updateTypoToleranceSettings(this.uid, typoTolerance);
    }

    /**
     * Resets the typo tolerance field of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-typo-tolerance">API
     *     specification</a>
     */
    public TaskInfo resetTypoToleranceSettings() throws MeilisearchException {
        return this.settingsHandler.resetTypoToleranceSettings(this.uid);
    }

    /**
     * Gets the pagination field of the index
     *
     * @return Pagination instance from Index
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-pagination-settings">API
     *     specification</a>
     */
    public Pagination getPaginationSettings() throws MeilisearchException {
        return this.settingsHandler.getPaginationSettings(this.uid);
    }

    /**
     * Updates the pagination field of the index
     *
     * @param pagination A pagination instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-pagination-settings">API
     *     specification</a>
     */
    public TaskInfo updatePaginationSettings(Pagination pagination) throws MeilisearchException {
        return this.settingsHandler.updatePaginationSettings(this.uid, pagination);
    }

    /**
     * Resets the pagination field of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-pagination-settings">API
     *     specification</a>
     */
    public TaskInfo resetPaginationSettings() throws MeilisearchException {
        return this.settingsHandler.resetPaginationSettings(this.uid);
    }

    /**
     * Gets the faceting field of the index
     *
     * @return Faceting instance from Index
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-faceting-settings">API
     *     specification</a>
     */
    public Faceting getFacetingSettings() throws MeilisearchException {
        return this.settingsHandler.getFacetingSettings(this.uid);
    }

    /**
     * Updates the faceting field of the index
     *
     * @param faceting A faceting instance
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/en/reference/api/settings#update-faceting-settings">API
     *     specification</a>
     */
    public TaskInfo updateFacetingSettings(Faceting faceting) throws MeilisearchException {
        return this.settingsHandler.updateFacetingSettings(this.uid, faceting);
    }

    /**
     * Resets the faceting field of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-faceting-settings">API
     *     specification</a>
     */
    public TaskInfo resetFacetingSettings() throws MeilisearchException {
        return this.settingsHandler.resetFacetingSettings(this.uid);
    }

    /**
     * Gets the dictionary settings of the index
     *
     * @return dictionary of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#get-dictionary">API
     *     specification</a>
     */
    public String[] getDictionarySettings() throws MeilisearchException {
        return this.settingsHandler.getDictionarySettings(this.uid);
    }

    /**
     * Updates the dictionary settings of the index
     *
     * @param dictionary An array of strings that contains the dictionary.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#update-dictionary">API
     *     specification</a>
     */
    public TaskInfo updateDictionarySettings(String[] dictionary) throws MeilisearchException {
        return this.settingsHandler.updateDictionarySettings(this.uid, dictionary);
    }

    /**
     * Resets the dictionary settings of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/settings#reset-dictionary">API
     *     specification</a>
     */
    public TaskInfo resetDictionarySettings() throws MeilisearchException {
        return this.settingsHandler.resetDictionarySettings(this.uid);
    }

    /**
     * Gets the proximity precision level of the index
     *
     * @return proximity precision level of a given uid as String
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#get-proximity-precision-settings">API
     *     specification</a>
     */
    public String getProximityPrecisionSettings() throws MeilisearchException {
        return this.settingsHandler.getProximityPrecisionSettings(this.uid);
    }

    /**
     * Updates the proximity precision level of the index
     *
     * @param proximityPrecision A String: the proximity precision level.
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#update-proximity-precision-settings">API
     *     specification</a>
     */
    public TaskInfo updateProximityPrecisionSettings(String proximityPrecision)
            throws MeilisearchException {
        return this.settingsHandler.updateProximityPrecisionSettings(this.uid, proximityPrecision);
    }

    /**
     * Resets the proximity precision level of the index
     *
     * @return TaskInfo instance
     * @throws MeilisearchException if an error occurs
     * @see <a
     *     href="https://www.meilisearch.com/docs/reference/api/settings#reset-proximity-precision-settings">API
     *     specification</a>
     */
    public TaskInfo resetProximityPrecisionSettings() throws MeilisearchException {
        return this.settingsHandler.resetProximityPrecisionSettings(this.uid);
    }

    /**
     * Gets extended information and metrics about indexes and the Meilisearch database
     *
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/stats#get-stats">API
     *     specification</a>
     */
    public IndexStats getStats() throws MeilisearchException {
        return this.instanceHandler.getIndexStats(this.uid);
    }

    /**
     * Retrieves an index tasks by its uid
     *
     * @param taskId Identifier of the requested index task
     * @return Task instance
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-one-task">API
     *     specification</a>
     */
    public Task getTask(int taskId) throws MeilisearchException {
        return this.tasksHandler.getTask(taskId);
    }

    /**
     * Retrieves list of tasks of the index
     *
     * @return List of tasks in the Meilisearch index
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-tasks">API
     *     specification</a>
     */
    public TasksResults getTasks() throws MeilisearchException {
        return this.tasksHandler.getTasks(this.uid);
    }

    /**
     * Retrieves list of tasks of the index
     *
     * @param param accept by the tasks route
     * @return List of tasks in the Meilisearch index
     * @throws MeilisearchException if an error occurs
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#get-tasks">API
     *     specification</a>
     */
    public TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        return this.tasksHandler.getTasks(this.uid, param);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskId Identifier of the requested Task
     * @throws MeilisearchException if an error occurs or if timeout is reached
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#task-status">API
     *     specification</a>
     */
    public void waitForTask(int taskId) throws MeilisearchException {
        this.tasksHandler.waitForTask(taskId, 5000, 50);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskId ID of the index update
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws MeilisearchException if an error occurs or if timeout is reached
     * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks#task-status">API
     *     specification</a>
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
