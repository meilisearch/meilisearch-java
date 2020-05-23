package meilisearch;

import com.google.gson.JsonObject;

class Index {
    Request request;
    Index (Config config) {
        request = new Request(config);
    }

    String create(String uid) throws Exception {
        return this.create(uid, null);
    }

    String create(String uid, String primaryKey) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", uid);
        if (primaryKey != null) {
            jsonObject.addProperty("primaryKey", primaryKey);
        }

        return request.post("/indexes", jsonObject.toString());
    }

    String get(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return request.get(requestQuery);
    }

    String getAll() throws Exception {
        return request.get("/indexes");
    }

    String update(String uid, String primaryKey) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("primaryKey", primaryKey);

        String requestQuery = "/indexes/" + uid;
        return request.put(requestQuery, jsonObject.toString());
    }

    String delete(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return request.delete(requestQuery);
    }
}
