package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.DocumentsQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import java.util.List;

/**
 * Class covering the Meilisearch Document API
 *
 * <p>https://docs.meilisearch.com/reference/api/documents.html
 */
class Documents {
    private final HttpClient httpClient;

    protected Documents(Config config) {
        httpClient = config.httpClient;
    }

    /**
     * Retrieves the document at the specified index uid with the specified identifier
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
        return httpClient.<T>get(new DocumentsQuery().toQuery(uid, identifier), targetClass);
    }

    /**
     * Retrieves the document at the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @param param accept by the documents route
     * @param targetClass Class of the document returned
     * @return Object containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    <T> T getDocument(String uid, String identifier, DocumentsQuery param, Class<T> targetClass)
            throws MeilisearchException {
        return httpClient.<T>get(param.toQuery(uid, identifier, param), targetClass);
    }

    /**
     * Retrieves the document at the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @return String containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    String getRawDocument(String uid, String identifier) throws MeilisearchException {
        return httpClient.<String>get(new DocumentsQuery().toQuery(uid, identifier), String.class);
    }

    /**
     * Retrieves the document at the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @param param accept by the documents route
     * @return String containing the requested document
     * @throws MeilisearchException if client request causes an error
     */
    String getRawDocument(String uid, String identifier, DocumentsQuery param)
            throws MeilisearchException {
        return httpClient.<String>get(param.toQuery(uid, identifier, param), String.class);
    }

    /**
     * Retrieves the document at the specified index
     *
     * @param <T> Type of documents returned
     * @param uid Partial index identifier for the requested documents
     * @param targetClass Class of documents returned
     * @return Object containing the requested document
     * @throws MeilisearchException if the client request causes an error
     */
    <T> Results<T> getDocuments(String uid, Class<T> targetClass) throws MeilisearchException {
        Results<T> documents =
                httpClient.<Results>get(
                        new DocumentsQuery().toQuery(uid), Results.class, targetClass);
        return documents;
    }

    /**
     * Retrieves the document at the specified index
     *
     * @param <T> Type of documents returned
     * @param uid Partial index identifier for the requested documents
     * @param param accept by the documents route
     * @param targetClass Class of documents returned
     * @return Object containing the requested document
     * @throws MeilisearchException if the client request causes an error
     */
    <T> Results<T> getDocuments(String uid, DocumentsQuery param, Class<T> targetClass)
            throws MeilisearchException {
        Results<T> documents =
                httpClient.<Results>get(param.toQuery(uid, param), Results.class, targetClass);
        return documents;
    }

    /**
     * Retrieves the document as String at the specified index
     *
     * @param uid Partial index identifier for the requested documents
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getRawDocuments(String uid) throws MeilisearchException {
        return httpClient.get(new DocumentsQuery().toQuery(uid), String.class);
    }

    /**
     * Retrieves the document as String at the specified index
     *
     * @param uid Partial index identifier for the requested documents
     * @param param accept by the documents route
     * @return Meilisearch API response
     * @throws MeilisearchException if an error occurs
     */
    String getRawDocuments(String uid, DocumentsQuery param) throws MeilisearchException {
        return httpClient.<String>get(param.toQuery(uid, param), String.class);
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
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid).addSubroute("documents");
        if (primaryKey != null) {
            urlb.addParameter("primaryKey", primaryKey);
        }
        String urlQuery = urlb.getURL();
        return httpClient.post(urlQuery, document, TaskInfo.class);
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
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes").addSubroute(uid).addSubroute("documents");
        if (primaryKey != null) {
            urlb.addParameter("primaryKey", primaryKey);
        }
        String urlPath = urlb.getURL();
        return httpClient.put(urlPath, document, TaskInfo.class);
    }

    /**
     * Deletes the document at the specified index uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested document
     * @param identifier ID of the document
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo deleteDocument(String uid, String identifier) throws MeilisearchException {
        return httpClient.<TaskInfo>delete(
                new DocumentsQuery().toQuery(uid, identifier), TaskInfo.class);
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
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("indexes")
                .addSubroute(uid)
                .addSubroute("documents")
                .addSubroute("delete-batch");
        String urlPath = urlb.getURL();
        return httpClient.post(urlPath, identifiers, TaskInfo.class);
    }

    /**
     * Deletes all documents at the specified index uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return Meilisearch's TaskInfo API response
     * @throws MeilisearchException if the client request causes an error
     */
    TaskInfo deleteAllDocuments(String uid) throws MeilisearchException {
        return httpClient.<TaskInfo>delete(new DocumentsQuery().toQuery(uid), TaskInfo.class);
    }
}
