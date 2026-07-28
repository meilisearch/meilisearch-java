package com.meilisearch.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.DynamicSearchRule;
import com.meilisearch.sdk.model.DynamicSearchRulesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class DynamicSearchRulesTest extends AbstractIT {

    @BeforeEach
    public void initialize() throws MeilisearchException {
        this.setUp();
        client.experimentalFeatures(Collections.singletonMap("dynamicSearchRules", true));
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    private DynamicSearchRule buildTestRule(String uid) {
        Map<String, Object> queryCondition = Map.of("scope", "query", "isEmpty", true);
        Map<String, Object> action =
                Map.of(
                        "selector", Map.of("indexUid", "products"),
                        "action", Map.of("type", "pin", "position", 1));

        return new DynamicSearchRule(
                uid, "Test rule", 5, true, List.of(queryCondition), List.of(action));
    }

    @Test
    public void testUpdateDynamicSearchRule() throws MeilisearchException {
        DynamicSearchRule rule = buildTestRule("test-rule");

        TaskInfo task = client.updateDynamicSearchRule("test-rule", rule);
        client.waitForTask(task.getTaskUid());

        DynamicSearchRule fetched = client.getDynamicSearchRule("test-rule");
        assertThat(fetched.getUid(), equalTo("test-rule"));
        assertThat(fetched.getDescription(), equalTo("Test rule"));
        assertThat(fetched.getPriority(), equalTo(5));
        assertThat(fetched.isActive(), equalTo(true));
    }

    @Test
    public void testGetDynamicSearchRule() throws MeilisearchException {
        DynamicSearchRule rule = buildTestRule("test-rule-get");
        TaskInfo task = client.updateDynamicSearchRule("test-rule-get", rule);
        client.waitForTask(task.getTaskUid());

        DynamicSearchRule fetched = client.getDynamicSearchRule("test-rule-get");
        assertThat(fetched, notNullValue());
        assertThat(fetched.getUid(), equalTo("test-rule-get"));
    }

    @Test
    public void testListDynamicSearchRules() throws MeilisearchException {
        TaskInfo task1 = client.updateDynamicSearchRule("rule-one", buildTestRule("rule-one"));
        client.waitForTask(task1.getTaskUid());
        TaskInfo task2 = client.updateDynamicSearchRule("rule-two", buildTestRule("rule-two"));
        client.waitForTask(task2.getTaskUid());

        Results<DynamicSearchRule> results =
                client.listDynamicSearchRules(new DynamicSearchRulesQuery());

        assertThat(results.getResults().length >= 2, equalTo(true));
    }

    @Test
    public void testDeleteDynamicSearchRule() throws MeilisearchException {
        DynamicSearchRule rule = buildTestRule("test-rule-delete");
        TaskInfo createTask = client.updateDynamicSearchRule("test-rule-delete", rule);
        client.waitForTask(createTask.getTaskUid());

        TaskInfo deleteTask = client.deleteDynamicSearchRule("test-rule-delete");
        client.waitForTask(deleteTask.getTaskUid());

        MeilisearchException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        MeilisearchException.class,
                        () -> client.getDynamicSearchRule("test-rule-delete"));
        assertThat(exception, notNullValue());
    }
}
