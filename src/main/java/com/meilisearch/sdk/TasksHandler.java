package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.exceptions.MeilisearchTimeoutException;
import com.meilisearch.sdk.model.Result;
import com.meilisearch.sdk.model.Task;
import java.util.Date;

/**
 * Wrapper around HttpClient class to use for MeiliSearch tasks
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/tasks.html
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
    public TasksHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Retrieves the Task at the specified indexUid with the specified taskUid
     *
     * @param indexUid Index identifier to the requested Task
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws MeilisearchException if client request causes an error
     */
    public Task getTask(String indexUid, int taskUid) throws MeilisearchException {
        String urlPath = "/indexes/" + indexUid + "/tasks/" + taskUid;
        return httpClient.get(urlPath, Task.class);
    }

    /**
     * Retrieves all TasksHandler at the specified iud
     *
     * @param indexUid Index identifier to the requested Tasks
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    public Result<Task> getTasks(String indexUid) throws MeilisearchException {
        String urlPath = "/indexes/" + indexUid + "/tasks";

        Result<Task> result = httpClient.get(urlPath, Result.class, Task.class);
        return result;
    }

    /**
     * Retrieves the Task at the specified iud with the specified taskUid
     *
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws MeilisearchException if client request causes an error
     */
    public Task getTask(int taskUid) throws MeilisearchException {
        String urlPath = "/tasks/" + taskUid;
        return httpClient.get(urlPath, Task.class);
    }

    /**
     * Retrieves Tasks from the client
     *
     * @return List of task instance
     * @throws MeilisearchException if client request causes an error
     */
    public Result<Task> getTasks() throws MeilisearchException {
        String urlPath = "/tasks";

        Result<Task> result = httpClient.get(urlPath, Result.class, Task.class);
        return result;
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskUid Identifier of the Task
     * @throws MeilisearchException if timeout is reached
     */
    public void waitForTask(int taskUid) throws MeilisearchException {
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
    public void waitForTask(int taskUid, int timeoutInMs, int intervalInMs)
            throws MeilisearchException {
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
