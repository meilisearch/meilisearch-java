package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum EmbedderSource {
    @SerializedName("openAi")
    OPEN_AI("openAi"),

    @SerializedName("huggingFace")
    HUGGING_FACE("huggingFace"),

    @SerializedName("ollama")
    OLLAMA("ollama"),

    @SerializedName("rest")
    REST("rest"),

    @SerializedName("userProvided")
    USER_PROVIDED("userProvided");

    public final String source;

    private EmbedderSource(String source) {
        this.source = source;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.source;
    }
}
