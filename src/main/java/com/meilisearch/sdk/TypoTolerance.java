package com.meilisearch.sdk;

import java.util.HashMap;
import org.json.JSONObject;

public class TypoTolerance {

    public TypoTolerance() {}

    protected boolean enabled = false;
    protected HashMap<String, Integer> minWordSizeForTypos;
    protected String[] disableOnWords;
    protected String[] disableOnAttributes;

    /**
     * Method to return the JSONObject of the TypoTolerance Setting
     *
     * @return JSONObject of the TypoTolerance Setting object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("enabled", this.enabled);
        jsonObject.put("minWordSizeForTypos", this.minWordSizeForTypos);
        jsonObject.put("disableOnWords", this.disableOnWords);
        jsonObject.put("disableOnAttributes", this.disableOnAttributes);
        return jsonObject;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getDisableOnWords() {
        return this.disableOnWords;
    }

    public void setDisableOnWords(String[] disableOnWords) {
        this.disableOnWords = disableOnWords;
    }

    public String[] getDisableOnAttributes() {
        return this.disableOnAttributes;
    }

    public void setDisableOnAttributes(String[] disableOnAttributes) {
        this.disableOnAttributes = disableOnAttributes;
    }

    public HashMap<String, Integer> getMinWordSizeForTypos() {
        return this.minWordSizeForTypos;
    }

    public void setMinWordSizeForTypos(HashMap<String, Integer> minWordSizeForTypos) {
        this.minWordSizeForTypos = minWordSizeForTypos;
    }
}
