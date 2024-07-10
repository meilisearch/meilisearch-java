package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

/** The code, type and error of the task error */
@Getter
@Setter
public class TaskError {

    public TaskError() {}

    protected String taskErrorCode = "";
    protected String taskErrorType = "";
    protected String taskErrorLink = "";
}
