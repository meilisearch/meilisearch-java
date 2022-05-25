package com.meilisearch.sdk;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class TypoTolerance {
    @Getter @Setter private boolean enabled = true;
    @Getter @Setter private HashMap<String, Integer> minWordSizeForTypos;
    @Getter @Setter private String[] disableOnWords;
    @Getter @Setter private String[] disableOnAttributes;

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
