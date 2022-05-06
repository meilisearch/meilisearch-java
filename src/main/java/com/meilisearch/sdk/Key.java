package com.meilisearch.sdk;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/** MeiliSearch response for a Key */
public class Key {
    @Getter @Setter protected String description = null;
    @Getter protected String key = "";
    @Getter @Setter protected String[] actions = null;
    @Getter @Setter protected String[] indexes = null;
    @Getter @Setter protected Date expiresAt = null;
    @Getter protected Date createdAt = null;
    @Getter protected Date updatedAt = null;

    /** Calls instance for MeiliSearch Key */
    public Key() {}

    /**
     * Method to return the JSON String of the Key
     *
     * @return JSON string of the Key object
     */
    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        return gson.toJson(this);
    }
}
