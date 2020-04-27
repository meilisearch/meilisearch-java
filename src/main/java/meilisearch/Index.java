package meilisearch;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class Index {
    Request request;
    Index (Config config) {
        request = new Request(config);
    }

    String create(String name, Schema schema) throws Exception {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(schema);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.add("schema", jsonElement);

        return request.post("/indexes", jsonObject.toString());
    }

    String get(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return request.get(requestQuery);
    }

    String getAll() throws Exception {
        return request.get("/indexes");
    }

    String update(String uid, String name) throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);

        String requestQuery = "/indexes/" + uid;
        return request.put(requestQuery, jsonObject.toString());
    }

    String delete(String uid) throws Exception {
        String requestQuery = "/indexes/" + uid;
        return request.delete(requestQuery);
    }
}
