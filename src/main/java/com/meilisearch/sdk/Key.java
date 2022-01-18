package com.meilisearch.sdk;

import com.google.gson.Gson;

/** MeiliSearch response for a Key */
public class Key {
    protected String description = "";
    protected String key = "";
    protected String[] actions = null;
    protected String[] indexes = null;
    protected String expiresAt = "";
    protected String createdAt = "";
    protected String updatedAt = "";

    private static Gson gsonKey = new Gson();

    /**
     * Method to return the JSON String of the Key
     *
     * @return JSON string of the Key object
     */
    @Override
    public String toString() {
        return gsonKey.toJson(this);
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
     * Method to return the description of the Key
     *
     * @return String containing the description of the Key
     */
    public String getDescription() {
        return this.description;
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
     * Method to return the indexes accessible by the Key
     *
     * @return String value of the indexes accessible by the Key
     */
    public String[] getIndexes() {
        return this.indexes;
    }

    /**
     * Method to return the time that the Key will expire
     *
     * @return String value of the date and time of expiration date of the Key
     */
    public String getExpiresAt() {
        return this.expiresAt;
    }

    /**
     * Method to return the time that the Key was created at
     *
     * @return String value of the date and time of the Key when it was created
     */
    public String getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Method to return the time that the Update was finished at
     *
     * @return String value of the date and time of the Update when it was finished
     */
    public String getUpdatedAt() {
        return this.updatedAt;
    }
}
