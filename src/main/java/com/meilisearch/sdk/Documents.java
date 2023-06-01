package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.DocumentQuery;
import com.meilisearch.sdk.model.DocumentsQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import java.util.List;

/**
 * Class covering the Meilisearch Document API
 *
 * <p>https://www.meilisearch.com/docs/reference/api/documents
 */
class Documents {
    private final HttpClient httpClient;

    /**
     * Creates and sets up an instance of Documents to simplify Meilisearch API calls to manage
     * documents
     *
     * @param config Meilisearch configuration
     */
    protected Documents(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Retrieves the document from the specified index uid with the specified identifier
     *
     * @param <T> Type of the document returned
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @param targetClass Class of the document returned
     * @return Object containing the requested document
     * @throws MeilisearchException if the client request causes an error
     */
    <T> T getDocument(String uid, String identifier, Class<T> targetClass)
            throws MeilisearchException {
        return httpClient.<T>get(documentPath(uid, identifier).getURL(), targetClass);
    }

    /**
     * Retrieves the document from the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @param param accepted by the get document route
     * @param targetClass Class of the document returned
     * @return Object containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    <T> T getDocument(String uid, String identifier, DocumentQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return httpClient.<T>get(
                documentPath(uid, identifier).addQuery(param.toQuery()).getURL(), targetClass);
    }

    /**
     * Retrieves the document from the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @return String containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    String getRawDocument(String uid, String identifier) throws MeilisearchException {
        return httpClient.<String>get(documentPath(uid, identifier).getURL(), String.class);
    }

    /**
     * Retrieves the document from the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @param param accept by the documents route
     * @return String containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    String getRawDocument(String uid, String identifier, DocumentQuery param)
            throws MeilisearchException {
        return httpClient.<String>get(
                documentPath(uid, identifier).addQuery(param.toQuery()).getURL(), String.class);
    }

    /**
     * Retrieves the document from the specified index
     *
     * @param <T> Type of documents returned
     * @param uid Partial index identifier for the requested documents
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if the client request causes an error
     */
    <T> Results<T> getDocuments(String uid, Class<T> targetClass) throws MeilisearchException {
        return httpClient.<Results>get(documentPath(uid).getURL(), Results.class, targetClass);
    }

    /**
     * Retrieves the document from the specified index
     *
     * @param <T> Type of documents returned
     * @param uid Partial index identifier for the requested documents
     * @param param accepted by the get documents route
     * @param targetClass Class of documents returned
     * @return Results containing a list of Object containing the requested document
     * @throws MeilisearchException if the client request causes an error
     */
    <T> Results<T> getDocuments(String uid, DocumentsQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return httpClient.<Results>get(
                documentPath(uid).addQuery(param.toQuery()).getURL(), Results.class, targetClass);
    }

    /**
     * Retrieves the document as a string from the specified index
     *
     * @param uid Partial index identifier for the requested documents
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getRawDocuments(String uid) throws MeilisearchException {
        return httpClient.get(documentPath(uid).getURL(), String.class);
    }

    /**
     * Retrieves the document as String from the specified index
     *
     * @param uid Partial index identifier for the requested documents
     * @param param accepted by the documents route
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getRawDocuments(String uid, DocumentsQuery param) throws MeilisearchException {
        return httpClient.<String>get(
                documentPath(uid).addQuery(param.toQuery()).getURL(), String.class);
    }

    /**
     * Adds/Replaces a document at the specified index uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to add
     * @param primaryKey PrimaryKey of the document
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo addDocuments(String uid, String document, String primaryKey)
            throws MeilisearchException {
        URLBuilder urlb = documentPath(uid);
        if (primaryKey != null) {
            urlb.addParameter("primaryKey", primaryKey);
        }
        return httpClient.post(urlb.getURL(), document, TaskInfo.class);
    }

    /**
     * Replaces a document at the specified index uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to replace the existing document
     * @param primaryKey PrimaryKey of the document
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo updateDocuments(String uid, String document, String primaryKey)
            throws MeilisearchException {
        URLBuilder urlb = documentPath(uid);
        if (primaryKey != null) {
            urlb.addParameter("primaryKey", primaryKey);
        }
        return httpClient.put(urlb.getURL(), document, TaskInfo.class);
    }

    /**
     * Deletes the document from the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested document
     * @param identifier ID of the document
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo deleteDocument(String uid, String identifier) throws MeilisearchException {
        return httpClient.<TaskInfo>delete(documentPath(uid, identifier).getURL(), TaskInfo.class);
    }

    /**
     * Deletes the documents at the specified index uid with the specified identifiers
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifiers ID of documents to delete
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo deleteDocuments(String uid, List<String> identifiers) throws MeilisearchException {
        URLBuilder urlb = documentPath(uid).addSubroute("delete-batch");
        return httpClient.post(urlb.getURL(), identifiers, TaskInfo.class);
    }

    /**
     * Deletes all documents at the specified index uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo deleteAllDocuments(String uid) throws MeilisearchException {
        return httpClient.<TaskInfo>delete(documentPath(uid).getURL(), TaskInfo.class);
    }

    /** Creates an URLBuilder for the constant route documents. */
    private URLBuilder documentPath(String uid) {
        return new URLBuilder().addSubroute("indexes").addSubroute(uid).addSubroute("documents");
    }

    /** Creates an URLBuilder for the constant route documents */
    private URLBuilder documentPath(String uid, String identifier) {
        return new URLBuilder()
                .addSubroute("indexes")
                .addSubroute(uid)
                .addSubroute("documents")
                .addSubroute(identifier);
    }
}
