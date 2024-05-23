package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.exceptions.MeilisearchTimeoutException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.*;
import java.util.Date;

/**
 * Class covering the Meilisearch Task API
 *
 * @see <a href="https://www.meilisearch.com/docs/reference/api/tasks">API specification</a>
 */
public class TasksHandler {
    private final HttpClient httpClient;

    /**
     * Creates and sets up an instance of Task to simplify MeiliSearch API calls to manage tasks
     *
     * @param config MeiliSearch configuration
     */
    protected TasksHandler(Config config) {
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
        URLBuilder urlb = new URLBuilder();
        urlb.addSubroute("tasks").addSubroute(Integer.toString(taskUid));
        String urlPath = urlb.getURL();
        return httpClient.get(urlPath, Task.class);
    }

    /**
     * Retrieves all tasks from the client
     *
     * @return TasksResults containing a list of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks() throws MeilisearchException {
        TasksResults result = httpClient.get(tasksPath().getURL(), TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from the client
     *
     * @param param accept by the tasks route
     * @return TasksResults containing a list of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(TasksQuery param) throws MeilisearchException {
        TasksResults result =
                httpClient.get(tasksPath().addQuery(param.toQuery()).getURL(), TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from specified index uid
     *
     * @param indexUid Index identifier to index of the requested Tasks
     * @return TasksResults containing a list of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(String indexUid) throws MeilisearchException {
        URLBuilder urlb = tasksPath().addParameter("indexUid", indexUid);

        TasksResults result = httpClient.get(urlb.getURL(), TasksResults.class);
        return result;
    }

    /**
     * Retrieves all tasks from specified index uid
     *
     * @param indexUid Index identifier to index of the requested Tasks
     * @param param accept by the tasks route
     * @return TasksResults containing a list of task instance
     * @throws MeilisearchException if client request causes an error
     */
    TasksResults getTasks(String indexUid, TasksQuery param) throws MeilisearchException {
        param = addIndexUidToQuery(indexUid, param);

        TasksResults result =
                httpClient.get(tasksPath().addQuery(param.toQuery()).getURL(), TasksResults.class);
        return result;
    }

    /**
     * Delete tasks from the client
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if client request causes an error
     */
    TaskInfo cancelTasks(CancelTasksQuery param) throws MeilisearchException {
        URLBuilder urlb = tasksPath().addSubroute("cancel");
        TaskInfo result =
                httpClient.post(urlb.addQuery(param.toQuery()).getURL(), null, TaskInfo.class);
        return result;
    }

    /**
     * Delete tasks from the client
     *
     * @param param accept by the tasks route
     * @return Meilisearch API response as TaskInfo
     * @throws MeilisearchException if client request causes an error
     */
    TaskInfo deleteTasks(DeleteTasksQuery param) throws MeilisearchException {
        TaskInfo result =
                httpClient.delete(tasksPath().addQuery(param.toQuery()).getURL(), TaskInfo.class);
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
        TaskStatus status = null;
        long startTime = new Date().getTime();
        long elapsedTime = 0;

        while (status == null
                || (status.equals(TaskStatus.ENQUEUED) || status.equals(TaskStatus.PROCESSING))) {
            if (elapsedTime >= timeoutInMs) {
                throw new MeilisearchTimeoutException();
            }
            task = this.getTask(taskUid);
            status = task.getStatus();
            try {
                Thread.sleep(intervalInMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new MeilisearchTimeoutException();
            }
            elapsedTime = new Date().getTime() - startTime;
        }
    }

    /** Creates an URLBuilder for the constant route tasks */
    private URLBuilder tasksPath() {
        return new URLBuilder("/tasks");
    }

    /** Add index uid to index uids list in task query */
    TasksQuery addIndexUidToQuery(String indexUid, TasksQuery param) {
        if (param != null && param.getIndexUids() != null) {
            String[] newIndexUid = new String[param.getIndexUids().length + 1];
            for (int i = 0; i < param.getIndexUids().length; i++)
                newIndexUid[i] = param.getIndexUids()[i];
            newIndexUid[param.getIndexUids().length] = indexUid;
            param.setIndexUids(newIndexUid);
        } else if (param != null) {
            param.setIndexUids(new String[] {indexUid});
        } else {
            param = new TasksQuery().setIndexUids(new String[] {indexUid});
        }
        return param;
    }
}
