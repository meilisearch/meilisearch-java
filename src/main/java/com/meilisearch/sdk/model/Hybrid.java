package com.meilisearch.sdk.model;

// @brunoocasali: I don't think we should use the fasterxml.jackson annotations across the library,
// since this should be customizable 🤔
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

/** Hybrid search configuration */
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hybrid {
    /** Semantic ratio for hybrid search (between 0 and 1) */
    private Double semanticRatio;

    /** Embedder to use for hybrid search (mandatory if hybrid is set) */
    private String embedder;

    /**
     * Method that returns the JSON representation of the Hybrid object
     *
     * @return JSONObject representation of the Hybrid object
     */
    public JSONObject toJSONObject() {
        return new JSONObject()
                .putOpt("semanticRatio", this.semanticRatio)
                .putOpt("embedder", this.embedder);
    }

    /**
     * Method that returns the JSON String of the Hybrid object
     *
     * @return JSON String of the Hybrid object
     */
    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
