package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.*;
import com.meilisearch.sdk.utils.Movie;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class TasksTest extends AbstractIT {

    private TestData<Movie> testData;

    @BeforeEach
    public void initialize() {
        this.setUp();
        if (testData == null) testData = this.getTestData(MOVIES_INDEX, Movie.class);
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Test Get Task */
    @Test
    public void testClientGetTask() throws Exception {
        String indexUid = "GetClientTask";
        TaskInfo response = client.createIndex(indexUid);
        client.waitForTask(response.getTaskUid());

        Task task = client.getTask(response.getTaskUid());

        assertTrue(task instanceof Task);
        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus());
        assertNotNull(task.getStartedAt());
        assertNotNull(task.getEnqueuedAt());
        assertNotNull(task.getFinishedAt());
        assertTrue(task.getUid() >= 0);
        assertNotNull(task.getDetails());
        assertNull(task.getDetails().getPrimaryKey());
    }

    /** Test Get Tasks */
    @Test
    public void testClientGetTasks() throws Exception {
        String indexUid = "GetClientTasks";
        TaskInfo response = client.createIndex(indexUid);
        client.waitForTask(response.getTaskUid());

        TasksResults result = client.getTasks();
        Task[] tasks = result.getResults();

        Task task = tasks[0];
        client.waitForTask(task.getUid());

        assertNotNull(task.getStatus());
        assertTrue(task.getUid() >= 0);
        assertNotNull(task.getDetails());
    }

    /** Test Get Tasks with limit */
    @Test
    public void testClientGetTasksLimit() throws Exception {
        int limit = 2;
        TasksQuery query = new TasksQuery().setLimit(limit);
        TasksResults result = client.getTasks(query);

        assertEquals(limit, result.getLimit());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Get Tasks with limit and from */
    @Test
    public void testClientGetTasksLimitAndFrom() throws Exception {
        int limit = 2;
        int from = 2;
        TasksQuery query = new TasksQuery().setLimit(limit).setFrom(from);
        TasksResults result = client.getTasks(query);

        assertEquals(limit, result.getLimit());
        assertEquals(from, result.getFrom());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Get Tasks with uid as filter */
    @Test
    public void testClientGetTasksWithUidFilter() throws Exception {
        TasksQuery query = new TasksQuery().setUids(new int[] {1});
        TasksResults result = client.getTasks(query);

        assertNotNull(result.getLimit());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Get Tasks with beforeEnqueuedAt as filter */
    @Test
    public void testClientGetTasksWithDateFilter() throws Exception {
        Date date = new Date();
        TasksQuery query = new TasksQuery().setBeforeEnqueuedAt(date);
        TasksResults result = client.getTasks(query);

        assertNotNull(result.getLimit());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Get Tasks with canceledBy as filter */
    @Test
    public void testClientGetTasksWithCanceledByFilter() throws Exception {
        TasksQuery query = new TasksQuery().setCanceledBy(new int[] {1});
        TasksResults result = client.getTasks(query);

        assertNotNull(result.getLimit());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Get Tasks with all query parameters */
    @Test
    public void testClientGetTasksAllQueryParameters() throws Exception {
        int limit = 2;
        int from = 2;
        TasksQuery query =
                new TasksQuery()
                        .setLimit(limit)
                        .setFrom(from)
                        .setStatuses(new String[] {"enqueued", "succeeded"})
                        .setTypes(new String[] {"indexDeletion"});
        TasksResults result = client.getTasks(query);

        assertEquals(limit, result.getLimit());
        assertNotNull(result.getFrom());
        assertNotNull(result.getNext());
        assertNotNull(result.getResults().length);
    }

    /** Test Cancel Task */
    @Test
    public void testClientCancelTask() throws Exception {
        CancelTasksQuery query =
                new CancelTasksQuery().setStatuses(new String[] {"enqueued", "succeeded"});

        TaskInfo task = client.cancelTasks(query);

        assertTrue(task instanceof TaskInfo);
        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus());
        assertNull(task.getIndexUid());
        assertEquals("taskCancelation", task.getType());
    }

    /** Test Cancel Task with multiple filters */
    @Test
    public void testClientCancelTaskWithMultipleFilters() throws Exception {
        Date date = new Date();
        CancelTasksQuery query =
                new CancelTasksQuery()
                        .setUids(new int[] {0, 1, 2})
                        .setStatuses(new String[] {"enqueued", "succeeded"})
                        .setTypes(new String[] {"indexDeletion"})
                        .setIndexUids(new String[] {"index"})
                        .setBeforeEnqueuedAt(date);

        TaskInfo task = client.cancelTasks(query);

        assertTrue(task instanceof TaskInfo);
        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus());
        assertNull(task.getIndexUid());
        assertEquals("taskCancelation", task.getType());
    }

    /** Test Delete Task */
    @Test
    public void testClientDeleteTask() throws Exception {
        DeleteTasksQuery query =
                new DeleteTasksQuery().setStatuses(new String[] {"enqueued", "succeeded"});

        TaskInfo task = client.deleteTasks(query);

        assertTrue(task instanceof TaskInfo);
        assertNotNull(task.getStatus());
        assertNotNull(task.getStatus());
        assertNull(task.getIndexUid());
        assertEquals("taskDeletion", task.getType());
    }

    /** Test Delete Task with multiple filters */
    @Test
    public void testClientDeleteTaskWithMultipleFilters() throws Exception {
        Date date = new Date();
        DeleteTasksQuery query =
                new DeleteTasksQuery()
                        .setUids(new int[] {0, 1, 2})
                        .setStatuses(new String[] {"enqueued", "succeeded"})
                        .setTypes(new String[] {"indexDeletion"})
                        .setIndexUids(new String[] {"index"})
                        .setBeforeEnqueuedAt(date);

        TaskInfo task = client.deleteTasks(query);

        assertTrue(task instanceof TaskInfo);
        assertNotNull(task.getStatus());
        assertNull(task.getIndexUid());
        assertEquals("taskDeletion", task.getType());
    }

    /** Test waitForTask */
    @Test
    public void testWaitForTask() throws Exception {
        String indexUid = "WaitForTask";
        TaskInfo response = client.createIndex(indexUid);
        client.waitForTask(response.getTaskUid());

        Task task = client.getTask(response.getTaskUid());

        assertTrue(task.getUid() >= 0);
        assertNotNull(task.getEnqueuedAt());
        assertNotNull(task.getStartedAt());
        assertNotNull(task.getFinishedAt());
        assertEquals(TaskStatus.SUCCEEDED, task.getStatus());
        assertNotNull(task.getDetails());
        assertNull(task.getDetails().getPrimaryKey());

        client.deleteIndex(task.getIndexUid());
    }

    /** Test waitForTask timeoutInMs */
    @Test
    public void testWaitForTaskTimoutInMs() throws Exception {
        String indexUid = "WaitForTaskTimoutInMs";
        Index index = client.index(indexUid);

        TaskInfo task = index.addDocuments(this.testData.getRaw());
        index.waitForTask(task.getTaskUid());

        assertThrows(Exception.class, () -> index.waitForTask(task.getTaskUid(), 0, 50));
    }
}
