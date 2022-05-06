package com.meilisearch.sdk;

import lombok.Getter;
import lombok.Setter;

/** The code, type and error of the task error */
public class TaskError {

    public TaskError() {}

    @Getter @Setter protected String taskErrorCode = "";
    @Getter @Setter protected String taskErrorType = "";
    @Getter @Setter protected String taskErrorLink = "";

   
}
