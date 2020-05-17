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

    String search (String uid, String q) throws Exception {
        String requestQuery = "/indexes/" + uid + "/search";
        SearchRequest sr = new SearchRequest(q);
        return request.get(requestQuery, sr.getQuery());
    }

    String search (String uid,
                   String q,
                   int offset,
                   int limit,
                   String attributesToRetrieve,
                   String attributesToCrop,
                   int cropLength,
                   String attributesToHighlight,
                   String filters,
                   boolean matches
    ) throws Exception {
        String requestQuery = "/indexes/" + uid + "/search";
        SearchRequest sr = new SearchRequest(q, offset, limit, attributesToRetrieve, attributesToCrop, cropLength, attributesToHighlight, filters, matches);
        return request.get(requestQuery, sr.getQuery());
    }

}
