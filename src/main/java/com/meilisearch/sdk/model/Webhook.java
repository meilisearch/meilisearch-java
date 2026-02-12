package com.meilisearch.sdk.model;


import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Webhook data structure.
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/webhooks">API
 * specification</a>
 */
public class Webhook implements Serializable {
    @Getter protected final UUID uuid;
    @Getter protected final String url;
    @Getter protected final HashMap<String, String> headers;
    @Getter protected final boolean isEditable;

    public Webhook(UUID uuid, String url, HashMap<String, String> headers, boolean isEditable) {
        this.uuid = uuid;
        this.url = url;
        this.headers = headers;
        this.isEditable = isEditable;
    }

}

