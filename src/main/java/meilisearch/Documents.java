package meilisearch;

class Documents {

    Request request;
    Documents (Config config) {
        request = new Request(config);
    }

    String getDocument (String uid, String identifier) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/" + identifier;
        return request.get(requestQuery);
    }

    String getDocuments (String uid) throws Exception {
        String requestQuery = "/indexes/" + uid + "/documents/";
        return request.get(requestQuery);
    }

}
