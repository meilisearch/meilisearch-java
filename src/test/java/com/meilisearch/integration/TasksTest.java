package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        this.setUpJacksonClient();
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

        assertThat(task, is(instanceOf(Task.class)));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getStartedAt(), is(notNullValue()));
        assertThat(task.getEnqueuedAt(), is(notNullValue()));
        assertThat(task.getFinishedAt(), is(notNullValue()));
        assertThat(task.getUid(), is(greaterThanOrEqualTo(0)));
        assertThat(task.getDetails(), is(notNullValue()));
        assertThat(task.getDetails().getPrimaryKey(), is(nullValue()));
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

        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getUid(), is(greaterThanOrEqualTo(0)));
        assertThat(task.getDetails(), is(notNullValue()));
    }

    /** Test Get Tasks with limit */
    @Test
    public void testClientGetTasksLimit() throws Exception {
        int limit = 2;
        TasksQuery query = new TasksQuery().setLimit(limit);
        TasksResults result = client.getTasks(query);

        assertThat(result.getLimit(), is(equalTo(limit)));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Task Error Values */
    @Test
    public void testClientGetTaskErrorValues() throws Exception {
        String indexUid = "CheckTaskErrorValues";
        Index index = client.index(indexUid);

        // Deleting all documents from an index that does not exist results in a task error.
        TaskInfo taskInfo = index.deleteAllDocuments();
        index.waitForTask(taskInfo.getTaskUid());

        Task task = client.getTask(taskInfo.getTaskUid());

        assertThat(task.getError(), is(notNullValue()));
        assertThat(task.getError().getCode(), not(blankOrNullString()));
        assertThat(task.getError().getType(), not(blankOrNullString()));
        assertThat(task.getError().getLink(), not(blankOrNullString()));
        assertThat(task.getError().getMessage(), not(blankOrNullString()));
    }

    /** Test Get Task Error Values When Adding Documents */
    @Test
    public void testClientGetTaskErrorWhenAddingDocuments() throws Exception {
        String indexUid = "CheckTaskErrorWhenAddingDocuments";
        Index index = client.index(indexUid);

        TaskInfo taskInfo = client.createIndex(indexUid);
        client.waitForTask(taskInfo.getTaskUid());

        String json = "{\"identifyer\": 1, \"name\": \"Donald Duck\"}";
        // Adding a document with a wrong identifier results in a task error.
        TaskInfo taskInfoAddDocuments = index.addDocuments(json, "identifier");
        client.waitForTask(taskInfoAddDocuments.getTaskUid());

        Task task = client.getTask(taskInfoAddDocuments.getTaskUid());

        assertThat(task.getError(), is(notNullValue()));
        assertThat(task.getError().getCode(), not(blankOrNullString()));
        assertThat(task.getError().getType(), not(blankOrNullString()));
        assertThat(task.getError().getLink(), not(blankOrNullString()));
        assertThat(task.getError().getMessage(), not(blankOrNullString()));
    }

    /** Test Get Tasks with limit and from */
    @Test
    public void testClientGetTasksLimitAndFrom() throws Exception {
        int limit = 2;
        int from = 2;
        TasksQuery query = new TasksQuery().setLimit(limit).setFrom(from);
        TasksResults result = client.getTasks(query);

        assertThat(result.getLimit(), is(equalTo(limit)));
        assertThat(result.getFrom(), is(equalTo(from)));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Tasks with uid as filter */
    @Test
    public void testClientGetTasksWithUidFilter() throws Exception {
        TasksQuery query = new TasksQuery().setUids(new int[] {1});
        TasksResults result = client.getTasks(query);

        assertThat(result.getLimit(), is(notNullValue()));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Tasks with beforeEnqueuedAt as filter */
    @Test
    public void testClientGetTasksWithDateFilter() throws Exception {
        Date date = new Date();
        TasksQuery query = new TasksQuery().setBeforeEnqueuedAt(date);
        TasksResults result = client.getTasks(query);

        assertThat(result.getLimit(), is(notNullValue()));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Tasks with canceledBy as filter */
    @Test
    public void testClientGetTasksWithCanceledByFilter() throws Exception {
        TasksQuery query = new TasksQuery().setCanceledBy(new int[] {1});
        TasksResults result = client.getTasks(query);

        assertThat(result.getLimit(), is(notNullValue()));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
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

        assertThat(result.getLimit(), is(equalTo(limit)));
        assertThat(result.getFrom(), is(notNullValue()));
        assertThat(result.getNext(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Cancel Task */
    @Test
    public void testClientCancelTask() throws Exception {
        CancelTasksQuery query =
                new CancelTasksQuery().setStatuses(new String[] {"enqueued", "succeeded"});

        TaskInfo task = client.cancelTasks(query);

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getIndexUid(), is(nullValue()));
        assertThat(task.getType(), is(equalTo("taskCancelation")));
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

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getIndexUid(), is(nullValue()));
        assertThat(task.getType(), is(equalTo("taskCancelation")));
    }

    /** Test Delete Task */
    @Test
    public void testClientDeleteTask() throws Exception {
        DeleteTasksQuery query =
                new DeleteTasksQuery().setStatuses(new String[] {"enqueued", "succeeded"});

        TaskInfo task = client.deleteTasks(query);

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getIndexUid(), is(nullValue()));
        assertThat(task.getType(), is(equalTo("taskDeletion")));
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

        assertThat(task, is(instanceOf(TaskInfo.class)));
        assertThat(task.getStatus(), is(notNullValue()));
        assertThat(task.getIndexUid(), is(nullValue()));
        assertThat(task.getType(), is(equalTo("taskDeletion")));
    }

    /** Test waitForTask */
    @Test
    public void testWaitForTask() throws Exception {
        String indexUid = "WaitForTask";
        TaskInfo response = client.createIndex(indexUid);
        client.waitForTask(response.getTaskUid());

        Task task = client.getTask(response.getTaskUid());

        assertThat(task.getUid(), is(greaterThanOrEqualTo(0)));
        assertThat(task.getEnqueuedAt(), is(notNullValue()));
        assertThat(task.getStartedAt(), is(notNullValue()));
        assertThat(task.getFinishedAt(), is(notNullValue()));
        assertThat(task.getStatus(), is(equalTo(TaskStatus.SUCCEEDED)));
        assertThat(task.getDetails(), is(notNullValue()));
        assertThat(task.getDetails().getPrimaryKey(), is(nullValue()));

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

    /** Test Tasks with Jackson Json Handler */
    @Test
    public void testTasksWithJacksonJsonHandler() throws Exception {
        String indexUid = "tasksWithJacksonJsonHandler";
        Index index = clientJackson.index(indexUid);

        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());

        assertThat(task.getStatus(), is(equalTo(TaskStatus.ENQUEUED)));
    }
}
