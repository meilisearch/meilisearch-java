package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;
import java.util.Date;

/** Wrapper around MeilisearchHttpRequest class to use for MeiliSearch updates */
public class TasksHandler {
    private final MeiliSearchHttpRequest meilisearchHttpRequest;

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
        String requestQuery = "/indexes/" + indexUid + "/tasks/" + taskUid;
        return new Gson().fromJson(this.meilisearchHttpRequest.get(requestQuery), Task.class);
    }

    /**
     * Retrieves all TasksHandler at the specified iud
     *
     * @param indexUid Index identifier to the requested Tasks
     * @return List of task instance
     * @throws Exception if client request causes an error
     */
    Task[] getTasks(String indexUid) throws Exception {
        String requestQuery = "/indexes/" + indexUid + "/tasks";
        return new Gson().fromJson(this.meilisearchHttpRequest.get(requestQuery), Task[].class);
    }

    /**
     * Retrieves the Task at the specified iud with the specified taskUid
     *
     * @param taskUid Identifier of the requested Task
     * @return Task instance
     * @throws Exception if client request causes an error
     */
    Task getTask(int taskUid) throws Exception, MeiliSearchApiException {
        String requestQuery = "/tasks/" + taskUid;
        return new Gson().fromJson(this.meilisearchHttpRequest.get(requestQuery), Task.class);
    }

    /**
     * Retrieves Tasks from the client
     *
     * @return List of task instance
     * @throws Exception if client request causes an error
     */
    Task[] getTasks() throws Exception {
        String requestQuery = "/tasks";
        Result result = new Result();
        result = new Gson().fromJson(this.meilisearchHttpRequest.get(requestQuery), Result.class);
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

        while (!status.equals("succeeded") && !status.equals("failed")) {
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
