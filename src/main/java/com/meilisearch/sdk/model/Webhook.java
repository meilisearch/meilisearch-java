package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;

/**
 * Webhook data structure.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/webhooks">API specification</a>
 */
public class Webhook implements Serializable {
    @Getter protected final UUID uuid;
    @Getter protected final String url;
    @Getter protected final HashMap<String, String> headers;
    protected final boolean isEditable;

    @JsonCreator
    public Webhook(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("url") String url,
            @JsonProperty("headers") HashMap<String, String> headers,
            @JsonProperty("isEditable") boolean isEditable) {
        this.uuid = uuid;
        this.url = url;
        this.headers = headers;
        this.isEditable = isEditable;
    }

    /** Named explicitly because Jackson would otherwise call this property {@code editable}. */
    @JsonProperty("isEditable")
    public boolean isEditable() {
        return isEditable;
    }
}
