package meilisearch;

import com.google.gson.JsonObject;

/**
 * Wrapper around the Request class to ease usage for Meilisearch indexes
 */
class IndexesHandler {
	Request request;
	IndexesHandler(MeilisearchConfig config) {
		request = new Request(config);
	}

	String create(String uid) throws Exception {
		return this.create(uid, null);
	}

	String create(String uid, String primaryKey) throws Exception {
		JsonObject params = new JsonObject();
		params.addProperty("uid", uid);
		if (primaryKey != null) {
			params.addProperty("primaryKey", primaryKey);
		}

		return request.post("/indexes", params.toString());
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
