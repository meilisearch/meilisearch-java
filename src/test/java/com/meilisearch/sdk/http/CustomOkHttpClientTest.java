package com.meilisearch.sdk.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.stream.Collectors;
import okhttp3.*;
import okhttp3.internal.connection.RealCall;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomOkHttpClientTest {
    private final Config config = new Config("http://localhost:7700", "masterKey");
    private final OkHttpClient client = mock(OkHttpClient.class);
    private final CustomOkHttpClient classToTest = new CustomOkHttpClient(config, client);

    private final ArrayDeque<Request> requestQueue = new ArrayDeque<>();
    private final ArrayDeque<Response> responseQueue = new ArrayDeque<>();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @BeforeEach
    void setUp() {
        RealCall mockCall = mock(RealCall.class);
        when(client.newCall(any()))
                .thenAnswer(
                        invocation -> {
                            Request request = invocation.getArgument(0);
                            requestQueue.push(request);

                            Response ok =
                                    new Response.Builder()
                                            .request(request)
                                            .protocol(Protocol.HTTP_1_1)
                                            .message("OK")
                                            .code(200)
                                            .headers(request.headers())
                                            .body(
                                                    ResponseBody.create(
                                                            readBody(request.body()), JSON))
                                            .build();

                            responseQueue.push(ok);

                            return mockCall;
                        });
        when(mockCall.execute()).then(invocation -> responseQueue.poll());
    }

    @AfterEach
    void tearDown() {
        requestQueue.clear();
        responseQueue.clear();
        reset(client);
    }

    private String readBody(RequestBody body) {
        if (body == null) {
            return "";
        }
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedReader(new InputStreamReader(buffer.inputStream()))
                .lines()
                .collect(Collectors.joining());
    }

    @Test
    void get() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.GET, "/test", Collections.emptyMap(), "some body");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.get(request);

        assertThat(response.getStatusCode(), equalTo(200));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }

    @Test
    void post() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.POST, "/test", Collections.emptyMap(), "some body");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.post(request);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContent(), equalTo(request.getContent()));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(request.getContent(), equalTo(readBody(expectedRequest.body())));
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }

    @Test
    void postWithoutBody() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.POST, "/test", Collections.emptyMap(), null);
        BasicHttpResponse response = (BasicHttpResponse) classToTest.post(request);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContent(), equalTo(""));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(readBody(expectedRequest.body()), equalTo(""));
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }

    @Test
    void put() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.PUT, "/test", Collections.emptyMap(), "some body");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.put(request);

        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getContent(), equalTo(request.getContent()));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(request.getContent(), equalTo(readBody(expectedRequest.body())));
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }

    @Test
    void delete() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.DELETE, "/test", Collections.emptyMap(), "some body");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.delete(request);

        assertThat(response.getStatusCode(), equalTo(200));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }

    @Test
    void deleteWithoutBody() throws Exception {
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.DELETE, "/test", Collections.emptyMap(), null);
        BasicHttpResponse response = (BasicHttpResponse) classToTest.delete(request);

        assertThat(response.getStatusCode(), equalTo(200));

        Request expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.method(), equalTo(request.getMethod().name()));
        assertThat(
                expectedRequest.url().toString(),
                equalTo(this.config.getHostUrl() + request.getPath()));
    }
}
