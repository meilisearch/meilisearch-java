package com.meilisearch.sdk.api.documents;

import com.meilisearch.sdk.ServiceTemplate;
import com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException;
import com.meilisearch.sdk.http.factory.RequestFactory;
import com.meilisearch.sdk.http.request.HttpMethod;
import java.util.Collections;
import java.util.List;

public class DocumentHandler<T> {
    private final ServiceTemplate serviceTemplate;
    private final RequestFactory requestFactory;
    private final String indexName;
    private final Class<?> indexModel;

    public DocumentHandler(
            ServiceTemplate serviceTemplate,
            RequestFactory requestFactory,
            String indexName,
            Class<?> indexModel) {
        this.serviceTemplate = serviceTemplate;
        this.requestFactory = requestFactory;
        this.indexName = indexName;
        this.indexModel = indexModel;
    }

    /**
     * Retrieve a document with a specific identifier
     *
     * @param identifier the identifier of the document you are looking for
     * @return the document specified by the identifier
     * @throws com.meilisearch.sdk.exceptions.MeiliSearchRuntimeException in case something went
     *     wrong (http error, json exceptions, etc)
     */
    public T getDocument(String identifier) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                indexModel);
    }

    /**
     * Retrieve a list of documents
     *
     * @return a list of Documents from the index.
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public List<T> getDocuments() throws MeiliSearchRuntimeException {
        return getDocuments(0);
    }

    /**
     * Retrieve a list of documents
     *
     * @param limit maximum number of documents to be returned
     * @return a list of Documents from the index.
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public List<T> getDocuments(int limit) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents";
        if (limit > 0) {
            requestQuery += "?limit=" + limit;
        }
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                List.class,
                indexModel);
    }

    /**
     * Add or replace a document
     *
     * @param data an already serialized document
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task addDocuments(String data) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.POST, requestQuery, Collections.emptyMap(), data),
                Task.class);
    }

    /**
     * Add or replace a batch of documents
     *
     * @param data a list of document objects
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task addDocuments(List<T> data) throws MeiliSearchRuntimeException {
        try {
            String dataString = serviceTemplate.getProcessor().encode(data);
            return addDocuments(dataString);
        } catch (Exception e) {
            throw new MeiliSearchRuntimeException(e);
        }
    }

    /**
     * Add or replace a document
     *
     * @param data the serialized document
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task replaceDocuments(String data) throws MeiliSearchRuntimeException {
        return addDocuments(data);
    }

    /**
     * Add or replace a batch of documents
     *
     * @param data a list of document objects
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task replaceDocuments(List<T> data) throws MeiliSearchRuntimeException {
        try {
            String dataString = serviceTemplate.getProcessor().encode(data);
            return replaceDocuments(dataString);
        } catch (Exception e) {
            throw new MeiliSearchRuntimeException(e);
        }
    }

    /**
     * Add or update a document
     *
     * @param data the serialized document
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task updateDocuments(String data) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents";
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.PUT, requestQuery, Collections.emptyMap(), data),
                Task.class);
    }

    /**
     * Add or update a document
     *
     * @param data a list of document objects
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task updateDocuments(List<T> data) throws MeiliSearchRuntimeException {
        try {
            String dataString = serviceTemplate.getProcessor().encode(data);
            return updateDocuments(dataString);
        } catch (Exception e) {
            throw new MeiliSearchRuntimeException(e);
        }
    }

    /**
     * Delete a document with a specific identifier
     *
     * @param identifier the id of the document
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task deleteDocument(String identifier) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents/" + identifier;
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Task.class);
    }

    /**
     * Delete a batch of documents
     *
     * @return an Task object with the taskId
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task deleteDocuments() throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/documents";
        return serviceTemplate.execute(
                requestFactory.create(
                        HttpMethod.DELETE, requestQuery, Collections.emptyMap(), null),
                Task.class);
    }

    /**
     * @param q the Querystring
     * @return a SearchResponse with the Hits represented by the mapped Class for this index
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public SearchResponse<T> search(String q) throws MeiliSearchRuntimeException {
        try {
            String requestQuery = "/indexes/" + indexName + "/search";
            SearchRequest sr = new SearchRequest(q);
            return serviceTemplate.execute(
                    requestFactory.create(
                            HttpMethod.POST,
                            requestQuery,
                            Collections.emptyMap(),
                            serviceTemplate.getProcessor().encode(sr)),
                    SearchResponse.class,
                    indexModel);
        } catch (Exception e) {
            throw new MeiliSearchRuntimeException(e);
        }
    }

    /**
     * @param sr SearchRequest
     * @return a SearchResponse with the Hits represented by the mapped Class for this index
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public SearchResponse<T> search(SearchRequest sr) throws MeiliSearchRuntimeException {
        try {
            String requestQuery = "/indexes/" + indexName + "/search";
            return serviceTemplate.execute(
                    requestFactory.create(
                            HttpMethod.POST,
                            requestQuery,
                            Collections.emptyMap(),
                            serviceTemplate.getProcessor().encode(sr)),
                    SearchResponse.class,
                    indexModel);
        }
		catch (Exception e) {
            throw new MeiliSearchRuntimeException(e);
        }
    }

    /**
     * Retrieve an task with a specific taskUid
     *
     * @param taskUid the taskUid
     * @return the task belonging to the taskUid
     * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
     *     exceptions, etc)
     */
    public Task getTask(int taskUid) throws MeiliSearchRuntimeException {
        String requestQuery = "/indexes/" + indexName + "/tasks/" + taskUid;
        return serviceTemplate.execute(
                requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(), null),
                Task.class);
    }

    // Must be reviewed when resolving the issue #315
    // /**
    //  * Retrieve a list containing all the tasks
    //  *
    //  * @return a List of Tasks
    //  * @throws MeiliSearchRuntimeException in case something went wrong (http error, json
    //  *     exceptions, etc)
    //  */
    // public List<Task> getTasks() throws MeiliSearchRuntimeException {
    //     String requestQuery = "/indexes/" + indexName + "/tasks";
    //     return serviceTemplate.execute(
    //             requestFactory.create(HttpMethod.GET, requestQuery, Collections.emptyMap(),
    // null),
    //             List.class,
    //             Task.class);
    // }
}
