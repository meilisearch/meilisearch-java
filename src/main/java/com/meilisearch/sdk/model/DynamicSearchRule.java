package com.meilisearch.sdk.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Meilisearch Dynamic Search Rule.
 *
 * @see <a href="https://www.meilisearch.com/docs/capabilities/search_rules/overview">API
 *     specification</a>
 */
@Getter
@Setter
public class DynamicSearchRule implements Serializable {
    protected String uid;
    protected String description;
    protected int priority;
    protected boolean active;
    protected List<Map<String, Object>> conditions;
    protected List<Map<String, Object>> actions;

    public DynamicSearchRule() {}

    public DynamicSearchRule(
            String uid,
            String description,
            int priority,
            boolean active,
            List<Map<String, Object>> conditions,
            List<Map<String, Object>> actions) {
        this.uid = uid;
        this.description = description;
        this.priority = priority;
        this.active = active;
        this.conditions = conditions;
        this.actions = actions;
    }
}
