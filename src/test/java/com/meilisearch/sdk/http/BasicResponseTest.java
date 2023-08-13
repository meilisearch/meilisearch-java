package com.meilisearch.sdk.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.meilisearch.sdk.http.response.BasicResponse;
import com.meilisearch.sdk.http.response.HttpResponse;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.Task;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class BasicResponseTest {

    private final BasicResponse basicResponse = new BasicResponse(new GsonJsonHandler());

    @Test
    void basicUseCase() {
        HttpResponse<String> response = new HttpResponse<>(null, 0, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertThat(httpResponse.hasContent(), is(equalTo(true)));
        assertThat(httpResponse.getContent(), is(emptyString()));
        assertThat(httpResponse.getHeaders(), is(nullValue()));
        assertThat(httpResponse.getStatusCode(), is(equalTo(0)));
    }

    @Test
    void headerMap() {
        Map<String, String> header =
                new HashMap<String, String>() {
                    {
                        put("thisisaheader", "header");
                    }
                };
        HttpResponse<String> response = new HttpResponse<>(header, 0, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertThat(httpResponse.hasContent(), is(equalTo(true)));
        assertThat(httpResponse.getHeaders(), is(equalTo(header)));
        assertThat(httpResponse.getStatusCode(), is(equalTo(0)));
    }

    @Test
    void statusCodeNotZero() {
        HttpResponse<String> response = new HttpResponse<>(null, 200, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertThat(httpResponse.hasContent(), is(equalTo(true)));
        assertThat(httpResponse.getContent(), is(emptyString()));
        assertThat(httpResponse.getHeaders(), is(nullValue()));
        assertThat(httpResponse.getStatusCode(), is(equalTo(200)));
    }

    @Test
    void contentString() {
        HttpResponse<String> response = new HttpResponse<>(null, 0, "thisisatest");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertThat(httpResponse.getHeaders(), is(nullValue()));
        assertThat(httpResponse.getStatusCode(), is(equalTo(0)));
        assertThat(httpResponse.hasContent(), is(equalTo(true)));
        assertThat(httpResponse.getContent(), is(equalTo("thisisatest")));
    }

    @Test
    @SuppressWarnings("unchecked")
    void contentClass() {
        String content =
                "{ \"uid\": 0, \"indexUid\": \"\", \"status\": \"\", \"type\": null, \"details\": null, \"duration\": \"\", \"enqueuedAt\": null, \"startedAt\": null, \"finishedAt\": null}";
        HttpResponse response = new HttpResponse(null, 0, content);
        HttpResponse<Task> httpResponse = basicResponse.create(response, Task.class);

        assertThat(httpResponse.getHeaders(), is(nullValue()));
        assertThat(httpResponse.getStatusCode(), is(equalTo(0)));
        assertThat(httpResponse.hasContent(), is(equalTo(true)));
        assertThat(httpResponse.getContent().getStatus(), is(equalTo(new Task().getStatus())));
        assertThat(httpResponse.getContent().getUid(), is(equalTo(new Task().getUid())));
        assertThat(httpResponse.getContent().getIndexUid(), is(equalTo(new Task().getIndexUid())));
        assertThat(httpResponse.getContent().getType(), is(equalTo(new Task().getType())));
        assertThat(httpResponse.getContent().getDuration(), is(equalTo(new Task().getDuration())));
        assertThat(
                httpResponse.getContent().getEnqueuedAt(), is(equalTo(new Task().getEnqueuedAt())));
        assertThat(
                httpResponse.getContent().getStartedAt(), is(equalTo(new Task().getStartedAt())));
        assertThat(
                httpResponse.getContent().getFinishedAt(), is(equalTo(new Task().getFinishedAt())));
        assertThat(httpResponse.getContent().getError(), is(equalTo(new Task().getError())));
        assertThat(httpResponse.getContent().getDetails(), is(equalTo(new Task().getDetails())));
        assertThat(httpResponse.getContent().getClass(), is(equalTo(Task.class)));
    }
}
