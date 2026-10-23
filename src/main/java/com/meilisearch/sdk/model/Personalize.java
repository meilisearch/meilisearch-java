package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

/** Personalization configuration for personalized search (experimental) */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Personalize {
    /** Natural-language description of the user's context used to personalize the search */
    private String userContext;

    /**
     * Method that returns the JSON representation of the Personalize object
     *
     * @return JSONObject representation of the Personalize object
     */
    public JSONObject toJSONObject() {
        return new JSONObject().putOpt("userContext", this.userContext);
    }

    /**
     * Method that returns the JSON String of the Personalize object
     *
     * @return JSON String of the Personalize object
     */
    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
