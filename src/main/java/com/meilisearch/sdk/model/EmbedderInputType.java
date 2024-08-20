package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum EmbedderInputType {
    @SerializedName("text")
    TEXT("text"),

    @SerializedName("textArray")
    TEXT_ARRAY("textArray");

    public final String inputType;

    private EmbedderInputType(String inputType) {
        this.inputType = inputType;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.inputType;
    }
}
