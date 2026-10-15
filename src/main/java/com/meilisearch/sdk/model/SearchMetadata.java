package com.meilisearch.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMetadata {
    private String queryUid;
    private String indexUid;
    private Map<String, Object> inlineRuleConditionOutcomes;

    public SearchMetadata() {}
}
