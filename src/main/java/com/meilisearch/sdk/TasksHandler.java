package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.exceptions.MeilisearchTimeoutException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TasksQuery;
import com.meilisearch.sdk.model.TasksResults;
import java.util.Date;

/**
 * Class covering the Meilisearch Task API
 *
 * <p>https://docs.meilisearch.com/reference/api/tasks.html
 */
public class TasksHandler {
    private final HttpClient httpClient;
    public static final String SUCCEEDED = "succeeded";
    public static final String FAILED = "failed";

    /**
     * Creates and sets up an instance of Task to simplify MeiliSearch API calls to manage tasks
     *
     * @param config MeiliSearch configuration
     */
    TasksHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Retrieves one task with the specified task uid
     *
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws MeilisearchException if client request causes an error
     */
    Task getTask(int taskUid) throws MeilisearchException {
        String urlPath = "/tasks/" + taskUid;
        return httpClient.get(urlPath, Task.class);
    }

    /**
     * Retrieves all tasks from the client
     *
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks() throws MeilisearchException {
        String urlPath = "/tasks";

        TasksResults result = httpClient.get(urlPath, TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from the client
     *
     * @param param accept by the tasks route
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("tasks")
                .addParameter("limit", param.getLimit())
                .addParameter("from", param.getFrom())
                .addParameter("status", param.getStatus())
                .addParameter("type", param.getType())
                .addParameter("indexUid", param.getIndexUid());
        String urlQuery = urlb.getURL();

        TasksResults result = httpClient.get(urlQuery, TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from specified index uid
     *
     * @param indexUid Index identifier to the requested Tasks
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(String indexUid) throws MeilisearchException {
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("tasks").addParameter("indexUid", indexUid);
        String urlQuery = urlb.getURL();

        TasksResults result = httpClient.get(urlQuery, TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from specified index uid
     *
     * @param indexUid Index identifier to the requested Tasks
     * @param param accept by the tasks route
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(String indexUid, TasksQuery param) throws MeilisearchException {
        String[] newIndexUid = new String[param.getIndexUid().length + 1];
        if (param != null && param.getIndexUid() != null) {
            for (int i = 0; i < param.getIndexUid().length; i++)
                newIndexUid[i] = param.getIndexUid()[i];
            newIndexUid[param.getIndexUid().length] = indexUid;
        }
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("tasks")
                .addParameter("limit", param.getLimit())
                .addParameter("from", param.getFrom())
                .addParameter("status", param.getStatus())
                .addParameter("type", param.getType())
                .addParameter("indexUid", newIndexUid);
        String urlQuery = urlb.getURL();

        TasksResults result = httpClient.get(urlQuery, TasksResults.class);
        return result;
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskUid Identifier of the Task
     * @throws MeilisearchException if timeout is reached
     */
    void waitForTask(int taskUid) throws MeilisearchException {
        this.waitForTask(taskUid, 5000, 50);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskUid Identifier of the Task
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws MeilisearchException if timeout is reached
     */
    void waitForTask(int taskUid, int timeoutInMs, int intervalInMs) throws MeilisearchException {
        Task task;
        String status = "";
        long startTime = new Date().getTime();
        long elapsedTime = 0;

        while (!status.equals(SUCCEEDED) && !status.equals(FAILED)) {
            if (elapsedTime >= timeoutInMs) {
                throw new MeilisearchTimeoutException();
            }
            task = this.getTask(taskUid);
            status = task.getStatus();
            try {
                Thread.sleep(intervalInMs);
            } catch (Exception e) {
                throw new MeilisearchTimeoutException();
            }
            elapsedTime = new Date().getTime() - startTime;
        }
    }
}
