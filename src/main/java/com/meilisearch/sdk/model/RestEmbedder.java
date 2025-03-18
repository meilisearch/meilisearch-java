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
public class RestEmbedder extends Embedder {
    /** Request configuration. Mandatory for REST embedder. */
    private Map<String, Object> request;

    /** Response configuration. Mandatory for REST embedder. */
    private Map<String, Object> response;

    /** HTTP headers. Optional. */
    private Map<String, String> headers;

    /** URL for the embedder service. */
    private String url;

    public RestEmbedder() {
        super();
        this.source = EmbedderSource.rest;
    }
}
