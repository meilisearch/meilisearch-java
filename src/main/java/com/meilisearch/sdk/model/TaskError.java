package com.meilisearch.sdk.model;

import lombok.Getter;
import lombok.Setter;

/** The code, type‚ error and message of the task error */
@Getter
@Setter
public class TaskError {

    public TaskError() {}

    protected String code = "";
    protected String type = "";
    protected String link = "";
    protected String message = "";
}
