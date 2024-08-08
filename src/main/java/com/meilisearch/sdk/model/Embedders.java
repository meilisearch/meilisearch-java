package com.meilisearch.sdk.model;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class Embedders {
    protected EmbedderSource source;
    protected String url;
    protected String apiKey;
    protected String model;
    protected String documentTemplate;
    protected Integer dimensions;
    protected String revision;
    protected String[] inputField;
    protected EmbedderInputType inputType;
    protected String query;

    public Embedders() {}
}
