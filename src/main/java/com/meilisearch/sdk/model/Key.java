package com.meilisearch.sdk.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Data structure of MeiliSearch response for a Key
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/keys.html
 */
@Getter
public class Key {
    @Setter protected String description = null;
    protected String key = "";
    @Setter protected String[] actions = null;
    @Setter protected String[] indexes = null;
    @Setter protected Date expiresAt = null;
    protected Date createdAt = null;
    protected Date updatedAt = null;

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
