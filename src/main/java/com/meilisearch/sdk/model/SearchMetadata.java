package com.meilisearch.sdk.model;

import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchMetadata {
    private String queryUid;
    private String indexUid;
    private Map<String, Object> inlineRuleConditionOutcomes;

    public SearchMetadata() {}
}
