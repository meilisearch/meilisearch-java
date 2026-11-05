package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;

/**
 * Data structure used in request body while creating or updating a webhook.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/webhooks">API Specification</a>
 */
@Getter
public class CreateUpdateWebhookRequest implements Serializable {
    final String url;

    // A null value is how the API removes a header, so it must stay in the body.
    @JsonInclude(content = JsonInclude.Include.ALWAYS)
    final HashMap<String, Object> headers;

    public CreateUpdateWebhookRequest(
            @NonNull String url, @NonNull HashMap<String, Object> headers) {
        this.url = url;
        this.headers = headers;
    }
}
