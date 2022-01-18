package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import java.util.Date;

/**
 * Wrapper around MeilisearchHttpRequest class to use for MeiliSearch tasks
 *
 * <p>Refer https://docs.meilisearch.com/reference/api/tasks.html
 */
public class TasksHandler {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;
    private final Gson gson = new Gson();
    public static final String SUCCEEDED = "succeeded";
    public static final String FAILED = "failed";

    /**
     * Creates and sets up an instance of Task to simplify MeiliSearch API calls to manage tasks
     *
     * @param config MeiliSearch configuration
     */
    public TasksHandler(Config config) {
        this.meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
    }

    /**
     * Retrieves the Task at the specified indexUid with the specified taskUid
     *
     * @param indexUid Index identifier to the requested Task
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws Exception if client request causes an error
     */
    Task getTask(String indexUid, int taskUid) throws Exception {
        String urlPath = "/indexes/" + indexUid + "/tasks/" + taskUid;
        return this.gson.fromJson(this.meilisearchHttpRequest.get(urlPath), Task.class);
    }

    /**
     * Retrieves all TasksHandler at the specified iud
     *
     * @param indexUid Index identifier to the requested Tasks
     * @return List of task instance
     * @throws Exception if client request causes an error
     */
    Task[] getTasks(String indexUid) throws Exception {
        String urlPath = "/indexes/" + indexUid + "/tasks";
        return this.gson.fromJson(this.meilisearchHttpRequest.get(urlPath), Task[].class);
    }

    /**
     * Retrieves the Task at the specified iud with the specified taskUid
     *
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws Exception if client request causes an error
     */
    Task getTask(int taskUid) throws Exception, MeiliSearchApiException {
        String urlPath = "/tasks/" + taskUid;
        return this.gson.fromJson(this.meilisearchHttpRequest.get(urlPath), Task.class);
    }

    /**
     * Retrieves Tasks from the client
     *
     * @return List of task instance
     * @throws Exception if client request causes an error
     */
    Task[] getTasks() throws Exception {
        String urlPath = "/tasks";

        Result<Task> result =
                this.gson.fromJson(
                        this.meilisearchHttpRequest.get(urlPath),
                        new TypeToken<Result<Task>>() {}.getType());

        return result.getResults();
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskUid Identifier of the Task
     * @throws Exception if timeout is reached
     */
    void waitForTask(int taskUid) throws Exception {
        this.waitForTask(taskUid, 5000, 50);
    }

    /**
     * Waits for a task to be processed
     *
     * @param taskUid Identifier of the Task
     * @param timeoutInMs number of milliseconds before throwing an Exception
     * @param intervalInMs number of milliseconds before requesting the status again
     * @throws Exception if timeout is reached
     */
    void waitForTask(int taskUid, int timeoutInMs, int intervalInMs) throws Exception {
        Task task;
        String status = "";
        long startTime = new Date().getTime();
        long elapsedTime = 0;

        while (!status.equals(SUCCEEDED) && !status.equals(FAILED)) {
            if (elapsedTime >= timeoutInMs) {
                throw new Exception();
            }
            task = this.getTask(taskUid);
            status = task.getStatus();
            Thread.sleep(intervalInMs);
            elapsedTime = new Date().getTime() - startTime;
        }
    }
}
