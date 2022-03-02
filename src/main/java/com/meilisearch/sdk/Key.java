package com.meilisearch.sdk;

import com.google.gson.*;
import java.util.Date;

/** MeiliSearch response for a Key */
public class Key {
    // This field has been set to "" pending resolution of this issue
    // https://github.com/meilisearch/meilisearch/issues/2116
    protected String description = null;
    protected String key = "";
    protected String[] actions = null;
    protected String[] indexes = null;
    protected Date expiresAt = null;
    protected Date createdAt = null;
    protected Date updatedAt = null;

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

    /**
     * Method to return the Key value
     *
     * @return String containing the value of the Key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Method to set the description of the key
     *
     * @param description A description for the key
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method to return the description of the Key
     *
     * @return String containing the description of the Key
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Method to set the actions authorized by the key
     *
     * @param actions An array of API actions permitted for the key.
     */
    public void setActions(String[] actions) {
        this.actions = actions;
    }

    /**
     * Method to return the actions authorized by the key
     *
     * @return String containing the actions authorized by the key
     */
    public String[] getActions() {
        return this.actions;
    }

    /**
     * Method to set the indexes accessible by the Key
     *
     * @param indexes An array of indexes the key is authorized to act on
     */
    public void setIndexes(String[] indexes) {
        this.indexes = indexes;
    }

    /**
     * Method to return the indexes accessible by the Key
     *
     * @return String value of the indexes accessible by the Key
     */
    public String[] getIndexes() {
        return this.indexes;
    }

    /**
     * Method to set the time that the Key will expire
     *
     * @param expiredAt Date and time when the key will expire, represented in ISO 8601 format
     */
    public void setExpiresAt(Date expiredAt) {
        this.expiresAt = expiredAt;
    }

    /**
     * Method to return the time that the Key will expire
     *
     * @return Date and time of expiration date of the Key
     */
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    /**
     * Method to return the time that the Key was created at
     *
     * @return Date and time of the Key when it was created
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Method to return the time that the Update was finished at
     *
     * @return Date and time of the Update when it was finished
     */
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
}
