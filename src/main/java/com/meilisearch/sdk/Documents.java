package com.meilisearch.sdk;

import static java.util.Collections.singletonList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.List;

/** Wrapper around MeilisearchHttpRequest class to use for MeiliSearch documents */
class Documents {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;

    Gson gson = new Gson();

    protected Documents(Config config) {
        meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Retrieves the document at the specified uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifier ID of the document
     * @return String containing the requested document
     * @throws Exception if client request causes an error
     */
    String getDocument(String uid, String identifier) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents/" + identifier;
        return meilisearchHttpRequest.get(urlPath);
    }

    /**
     * Retrieves the documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return String containing the requested document
     * @throws Exception if the client request causes an error
     */
    String getDocuments(String uid) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents";
        return meilisearchHttpRequest.get(urlPath);
    }

    /**
     * Retrieves the documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @param limit Limit on the requested documents to be returned
     * @return String containing the requested document
     * @throws Exception if the client request causes an error
     */
    String getDocuments(String uid, int limit) throws Exception {
        String urlQuery = "/indexes/" + uid + "/documents?limit=" + limit;
        return meilisearchHttpRequest.get(urlQuery);
    }

    /**
     * Retrieves the documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @param limit Limit on the requested documents to be returned
     * @param offset Specify the offset of the first hit to return
     * @return String containing the requested document
     * @throws Exception if the client request causes an error
     */
    String getDocuments(String uid, int limit, int offset) throws Exception {
        String urlQuery = "/indexes/" + uid + "/documents?limit=" + limit + "&offset=" + offset;
        return meilisearchHttpRequest.get(urlQuery);
    }

    /**
     * Retrieves the documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @param limit Limit on the requested documents to be returned
     * @param offset Specify the offset of the first hit to return
     * @param attributesToRetrieve Document attributes to show
     * @return String containing the requested document
     * @throws Exception if the client request causes an error
     */
    String getDocuments(String uid, int limit, int offset, List<String> attributesToRetrieve)
            throws Exception {
        if (attributesToRetrieve == null || attributesToRetrieve.size() == 0) {
            attributesToRetrieve = singletonList("*");
        }

        String attributesToRetrieveCommaSeparated = String.join(",", attributesToRetrieve);
        String urlQuery =
                "/indexes/"
                        + uid
                        + "/documents?limit="
                        + limit
                        + "&offset="
                        + offset
                        + "&attributesToRetrieve="
                        + attributesToRetrieveCommaSeparated;

        return meilisearchHttpRequest.get(urlQuery);
    }

    /**
     * Adds/Replaces a document at the specified uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to add
     * @param primaryKey PrimaryKey of the document
     * @return MeiliSearch's Task API response
     * @throws Exception if the client request causes an error
     */
    Task addDocuments(String uid, String document, String primaryKey) throws Exception {
        String urlQuery = "/indexes/" + uid + "/documents";
        if (primaryKey != null) {
            urlQuery += "?primaryKey=" + primaryKey;
        }

        Task task = gson.fromJson(meilisearchHttpRequest.post(urlQuery, document), Task.class);
        return task;
    }

    /**
     * Replaces a document at the specified uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to replace the existing document
     * @param primaryKey PrimaryKey of the document
     * @return MeiliSearch's Task API response
     * @throws Exception if the client request causes an error
     */
    Task updateDocuments(String uid, String document, String primaryKey) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents";
        if (primaryKey != null) {
            urlPath += "?primaryKey=" + primaryKey;
        }

        Task task = gson.fromJson(meilisearchHttpRequest.put(urlPath, document), Task.class);
        return task;
    }

    /**
     * Deletes the document at the specified uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested document
     * @param identifier ID of the document
     * @return MeiliSearch's Task API response
     * @throws Exception if the client request causes an error
     */
    Task deleteDocument(String uid, String identifier) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents/" + identifier;

        Task task = gson.fromJson(meilisearchHttpRequest.delete(urlPath), Task.class);
        return task;
    }

    /**
     * Deletes the documents at the specified uid with the specified identifiers
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifiers ID of documents to delete
     * @return MeiliSearch's Task API response
     * @throws Exception if the client request causes an error
     */
    Task deleteDocuments(String uid, List<String> identifiers) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents/" + "delete-batch";
        JsonArray requestData = new JsonArray(identifiers.size());
        identifiers.forEach(requestData::add);

        Task task =
                gson.fromJson(
                        meilisearchHttpRequest.post(urlPath, requestData.toString()), Task.class);
        return task;
    }

    /**
     * Deletes all documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return MeiliSearch's Task API response
     * @throws Exception if the client request causes an error
     */
    Task deleteAllDocuments(String uid) throws Exception {
        String urlPath = "/indexes/" + uid + "/documents";

        Task task = gson.fromJson(meilisearchHttpRequest.delete(urlPath), Task.class);
        return task;
    }
}
