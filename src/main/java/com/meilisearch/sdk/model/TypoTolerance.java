package com.meilisearch.sdk.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Data structure for the Typo tolerance setting
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/typo_tolerance.html
 */
@Getter
@Setter
public class TypoTolerance {
    protected boolean enabled = true;
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
}
