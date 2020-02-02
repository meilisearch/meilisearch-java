package meilisearch;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class Indexes {
    Request request;
    Indexes (Config config) {
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
}
