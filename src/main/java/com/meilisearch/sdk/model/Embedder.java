package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Embedder {
    /** Source of the embedder. Accepts: ollama, rest, openAI, huggingFace and userProvided */
    protected EmbedderSource source;

    /**
     * API key for authentication with the embedder service. Optional: Only applicable for openAi,
     * ollama, and rest sources.
     */
    protected String apiKey;

    /**
     * Model to use for generating embeddings. Optional: Only applicable for ollama, openAI, and
     * huggingFace sources.
     */
    protected String model;

    /** Template for document embedding. Optional. */
    protected String documentTemplate;

    /**
     * Dimensions of the embedding vectors. Optional: Only applicable for openAi, huggingFace,
     * ollama, and rest sources.
     */
    protected Integer dimensions;

    /** Distribution configuration. Optional. */
    protected EmbedderDistribution distribution;

    /** Request configuration. Mandatory only when using rest embedder, optional otherwise. */
    protected Map<String, Object> request;

    /** Response configuration. Mandatory only when using rest embedder, optional otherwise. */
    protected Map<String, Object> response;

    /** Maximum bytes for document template. Optional. */
    protected Integer documentTemplateMaxBytes;

    /** Revision identifier. Optional: Only applicable for huggingFace. */
    protected String revision;

    /** HTTP headers. Optional: Only applicable for rest. */
    protected Map<String, String> headers;

    /** Whether to use binary quantization. Optional. */
    protected Boolean binaryQuantized;

    /** URL for the embedder service. Optional. */
    protected String url;

    /** Input fields for the embedder. Optional. */
    protected String[] inputField;

    /** Type of input for the embedder. Optional. */
    protected EmbedderInputType inputType;

    /** Query for the embedder. Optional. */
    protected String query;

    public Embedder() {}
}
