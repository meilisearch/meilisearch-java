package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RenderTemplateResult {
    private Object template;
    private Object rendered;

    public RenderTemplateResult() {}
}
