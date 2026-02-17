package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import lombok.NonNull;

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
}
