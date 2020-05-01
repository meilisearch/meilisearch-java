package meilisearch;

class Documents {

    Request request;
    protected Documents (Config config) {
        request = new Request(config);
    }

    String getDocument (String uid, String identifier) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
        return request.get(requestQuery);
    }

    String getDocuments (String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        return request.get(requestQuery);
    }

    String getDocuments (String uid, int limit) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents?limit=" + limit;
        return request.get(requestQuery);
    }

    String addDocument (String uid, String document) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        return request.post(requestQuery, document);
    }

    String updateDocument (String uid, String document) throws Exception {
        // TODO
        return "";
    }

    String deleteDocument (String uid, String identifier) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
        return request.delete(requestQuery);
    }

    String deleteDocuments (String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents";
        return request.delete(requestQuery);
    }

    String deleteList (String uid, int[] ids) throws Exception {
        // TODO
        return "";
    }

}
