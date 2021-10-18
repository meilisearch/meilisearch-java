package com.meilisearch.sdk;

import static java.util.Collections.singletonList;

import com.google.gson.JsonArray;
import java.util.List;

/** Wrapper around MeilisearchHttpRequest class to use for MeiliSearch documents */
class Documents {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;

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
        String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
        return meilisearchHttpRequest.get(requestQuery);
    }

    /**
     * Retrieves the documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return String containing the requested document
     * @throws Exception if the client request causes an error
     */
    String getDocuments(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        return meilisearchHttpRequest.get(requestQuery);
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
        String requestQuery = "/indexes/" + uid + "/documents?limit=" + limit;
        return meilisearchHttpRequest.get(requestQuery);
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
        String requestQuery = "/indexes/" + uid + "/documents?limit=" + limit + "&offset=" + offset;
        return meilisearchHttpRequest.get(requestQuery);
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
        String requestQuery =
                "/indexes/"
                        + uid
                        + "/documents?limit="
                        + limit
                        + "&offset="
                        + offset
                        + "&attributesToRetrieve="
                        + attributesToRetrieveCommaSeparated;

        return meilisearchHttpRequest.get(requestQuery);
    }

    /**
     * Adds/Replaces a document at the specified uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to add
     * @param primaryKey PrimaryKey of the document
     * @return String containing the added document
     * @throws Exception if the client request causes an error
     */
    String addDocuments(String uid, String document, String primaryKey) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        if (primaryKey != null) {
            requestQuery += "?primaryKey=" + primaryKey;
        }
        return meilisearchHttpRequest.post(requestQuery, document);
    }

    /**
     * Replaces a document at the specified uid
     *
     * @param uid Partial index identifier for the document
     * @param document String containing the document to replace the existing document
     * @param primaryKey PrimaryKey of the document
     * @return String containing the added document
     * @throws Exception if the client request causes an error
     */
    String updateDocuments(String uid, String document, String primaryKey) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        if (primaryKey != null) {
            requestQuery += "?primaryKey=" + primaryKey;
        }
        return meilisearchHttpRequest.put(requestQuery, document);
    }

    /**
     * Deletes the document at the specified uid with the specified identifier
     *
     * @param uid Partial index identifier for the requested document
     * @param identifier ID of the document
     * @return the corresponding updateId JSON
     * @throws Exception if the client request causes an error
     */
    String deleteDocument(String uid, String identifier) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
        return meilisearchHttpRequest.delete(requestQuery);
    }

    /**
     * Deletes the documents at the specified uid with the specified identifiers
     *
     * @param uid Partial index identifier for the requested documents
     * @param identifiers ID of documents to delete
     * @return the corresponding updateId JSON
     * @throws Exception if the client request causes an error
     */
    String deleteDocuments(String uid, List<String> identifiers) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/" + "delete-batch";
        JsonArray requestData = new JsonArray(identifiers.size());
        identifiers.forEach(requestData::add);

        return meilisearchHttpRequest.post(requestQuery, requestData.toString());
    }

    /**
     * Deletes all documents at the specified uid
     *
     * @param uid Partial index identifier for the requested documents
     * @return the corresponding updateId JSON
     * @throws Exception if the client request causes an error
     */
    String deleteAllDocuments(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        return meilisearchHttpRequest.delete(requestQuery);
    }
}
