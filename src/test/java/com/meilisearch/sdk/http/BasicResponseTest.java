package com.meilisearch.sdk.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        HttpResponse<String> response = new HttpResponse<String>(null, 0, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertTrue(httpResponse.hasContent());
        assertEquals(httpResponse.getContent(), "");
        assertNull(httpResponse.getHeaders());
        assertEquals(httpResponse.getStatusCode(), 0);
    }

    @Test
    void headerMap() {
        Map<String, String> header =
                new HashMap<String, String>() {
                    {
                        put("thisisaheader", "header");
                    }
                };
        HttpResponse<String> response = new HttpResponse<String>(header, 0, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertTrue(httpResponse.hasContent());
        assertEquals(httpResponse.getHeaders(), header);
        assertEquals(httpResponse.getStatusCode(), 0);
    }

    @Test
    void statusCodeNotZero() {
        HttpResponse<String> response = new HttpResponse<String>(null, 200, "");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertTrue(httpResponse.hasContent());
        assertEquals(httpResponse.getContent(), "");
        assertNull(httpResponse.getHeaders());
        assertEquals(httpResponse.getStatusCode(), 200);
    }

    @Test
    void contentString() {
        HttpResponse<String> response = new HttpResponse<String>(null, 0, "thisisatest");
        HttpResponse<String> httpResponse = basicResponse.create(response, String.class);

        assertNull(httpResponse.getHeaders());
        assertEquals(httpResponse.getStatusCode(), 0);
        assertTrue(httpResponse.hasContent());
        assertEquals(httpResponse.getContent(), "thisisatest");
    }

    @Test
    void contentClass() {
        String content =
                "{ \"uid\": 0, \"indexUid\": \"\", \"status\": \"\", \"type\": null, \"details\": null, \"duration\": \"\", \"enqueuedAt\": null, \"startedAt\": null, \"finishedAt\": null}";
        HttpResponse response = new HttpResponse(null, 0, content);
        HttpResponse<Task> httpResponse = basicResponse.create(response, Task.class);

        assertNull(httpResponse.getHeaders());
        assertEquals(httpResponse.getStatusCode(), 0);
        assertTrue(httpResponse.hasContent());
        assertEquals(httpResponse.getContent().getStatus(), new Task().getStatus());
        assertEquals(httpResponse.getContent().getUid(), new Task().getUid());
        assertEquals(httpResponse.getContent().getIndexUid(), new Task().getIndexUid());
        assertEquals(httpResponse.getContent().getType(), new Task().getType());
        assertEquals(httpResponse.getContent().getDuration(), new Task().getDuration());
        assertEquals(httpResponse.getContent().getEnqueuedAt(), new Task().getEnqueuedAt());
        assertEquals(httpResponse.getContent().getStartedAt(), new Task().getStartedAt());
        assertEquals(httpResponse.getContent().getFinishedAt(), new Task().getFinishedAt());
        assertEquals(httpResponse.getContent().getError(), new Task().getError());
        assertEquals(httpResponse.getContent().getDetails(), new Task().getDetails());
        assertEquals(httpResponse.getContent().getClass(), Task.class);
    }
}
