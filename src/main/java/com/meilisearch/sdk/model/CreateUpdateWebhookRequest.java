package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Data structure used in request body while creating or updating a webhook.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/webhooks">API Specification</a>
 */
public class CreateUpdateWebhookRequest implements Serializable {
    final String url;
    final HashMap<String, Object> headers;

    public CreateUpdateWebhookRequest(
            @NonNull String url, @NonNull HashMap<String, Object> headers) {
        this.url = url;
        this.headers = headers;
    }

    /**
     * Builds the JSON request body. Headers set to {@code null} are kept in the body, because that
     * is how the API removes a header from a webhook.
     */
    @Override
    public String toString() {
        JSONObject jsonHeaders = new JSONObject();
        headers.forEach(
                (name, value) -> jsonHeaders.put(name, value == null ? JSONObject.NULL : value));

        return new JSONObject().put("url", url).put("headers", jsonHeaders).toString();
    }
}
