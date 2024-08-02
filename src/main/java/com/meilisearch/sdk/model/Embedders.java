package com.meilisearch.sdk.model;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@Accessors(chain = true)
public class Embedders {
    protected String source;
    protected String model;
    protected String documentTemplate;
    protected Integer dimensions;

    public Embedders() {}
}
