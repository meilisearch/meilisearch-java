package com.meilisearch.sdk.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.http.request.BasicHttpRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.response.BasicHttpResponse;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import org.apache.hc.client5.http.async.HttpAsyncClient;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApacheHttpClientTest {
    private final Config config = new Config("http://localhost:7700", "masterKey");
    private final HttpAsyncClient client = mock(HttpAsyncClient.class);
    private final ApacheHttpClient classToTest = new ApacheHttpClient(config, client);

    private final ArrayDeque<SimpleHttpRequest> requestQueue = new ArrayDeque<>();
    private final ArrayDeque<SimpleHttpResponse> responseQueue = new ArrayDeque<>();

    @BeforeEach
    void setUp() {
        when(client.execute(any(), any(), any(), any(), any()))
                .then(
                        invocation -> {
                            SimpleRequestProducer argument = invocation.getArgument(0);
                            Field requestField =
                                    BasicRequestProducer.class.getDeclaredField("request");
                            requestField.setAccessible(true);
                            SimpleHttpRequest request =
                                    (SimpleHttpRequest) requestField.get(argument);
                            requestQueue.push(request);
                            SimpleHttpResponse polledResponse = responseQueue.poll();
                            //noinspection unchecked
                            ((FutureCallback<SimpleHttpResponse>) invocation.getArgument(4))
                                    .completed(polledResponse);
                            return CompletableFuture.completedFuture(polledResponse);
                        });
    }

    @AfterEach
    void tearDown() {
        requestQueue.clear();
        responseQueue.clear();
        reset(client);
    }

    @Test
    void get() throws Exception {
        SimpleHttpResponse expectedResponse =
                SimpleHttpResponse.create(200, "some body", ContentType.APPLICATION_JSON);
        responseQueue.push(expectedResponse);
        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.GET, "/test", Collections.emptyMap(), "thisisabody");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.get(request);

        assertThat(response.getStatusCode(), equalTo(expectedResponse.getCode()));
        assertThat(response.getContentAsBytes(), equalTo(expectedResponse.getBodyBytes()));

        SimpleHttpRequest expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.getBodyText(), equalTo(request.getContent()));
        assertThat(expectedRequest.getMethod(), equalTo(request.getMethod().name()));
        assertThat(expectedRequest.getPath(), equalTo(request.getPath()));
    }

    @Test
    void post() throws Exception {
        SimpleHttpResponse expectedResponse =
                SimpleHttpResponse.create(200, "some body", ContentType.APPLICATION_JSON);
        responseQueue.push(expectedResponse);
        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.POST, "/test", Collections.emptyMap(), "thisisabody");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.post(request);

        assertThat(response.getStatusCode(), equalTo(expectedResponse.getCode()));
        assertThat(response.getContentAsBytes(), equalTo(expectedResponse.getBodyBytes()));

        SimpleHttpRequest expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.getBodyText(), equalTo(request.getContent()));
        assertThat(expectedRequest.getMethod(), equalTo(request.getMethod().name()));
        assertThat(expectedRequest.getPath(), equalTo(request.getPath()));
    }

    @Test
    void put() throws Exception {
        SimpleHttpResponse expectedResponse =
                SimpleHttpResponse.create(200, "some body", ContentType.APPLICATION_JSON);
        responseQueue.push(expectedResponse);
        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.PUT, "/test", Collections.emptyMap(), "thisisabody");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.put(request);

        assertThat(response.getStatusCode(), equalTo(expectedResponse.getCode()));
        assertThat(response.getContentAsBytes(), equalTo(expectedResponse.getBodyBytes()));

        SimpleHttpRequest expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.getBodyText(), equalTo(request.getContent()));
        assertThat(expectedRequest.getMethod(), equalTo(request.getMethod().name()));
        assertThat(expectedRequest.getPath(), equalTo(request.getPath()));
    }

    @Test
    void patch() throws Exception {
        SimpleHttpResponse expectedResponse =
                SimpleHttpResponse.create(200, "some body", ContentType.APPLICATION_JSON);
        responseQueue.push(expectedResponse);
        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.PUT, "/test", Collections.emptyMap(), "thisisabody");
        BasicHttpResponse response = (BasicHttpResponse) classToTest.patch(request);

        assertThat(response.getStatusCode(), equalTo(expectedResponse.getCode()));
        assertThat(response.getContentAsBytes(), equalTo(expectedResponse.getBodyBytes()));

        SimpleHttpRequest expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.getBodyText(), equalTo(request.getContent()));
        assertThat(expectedRequest.getMethod(), equalTo(request.getMethod().name()));
        assertThat(expectedRequest.getPath(), equalTo(request.getPath()));
    }

    @Test
    void delete() throws Exception {
        SimpleHttpResponse expectedResponse = SimpleHttpResponse.create(204);
        responseQueue.push(expectedResponse);
        BasicHttpRequest request =
                new BasicHttpRequest(HttpMethod.DELETE, "/test", Collections.emptyMap(), null);
        BasicHttpResponse response = (BasicHttpResponse) classToTest.delete(request);

        assertThat(response.getStatusCode(), equalTo(expectedResponse.getCode()));
        assertThat(response.hasContent(), equalTo(false));

        SimpleHttpRequest expectedRequest = requestQueue.poll();
        assertThat(expectedRequest, notNullValue());
        assertThat(expectedRequest.getMethod(), equalTo(request.getMethod().name()));
        assertThat(expectedRequest.getPath(), equalTo(request.getPath()));
    }

    @Test
    void withCancelledRequest() {
        reset(client);
        when(client.execute(any(), any(), any(), any(), any()))
                .then(
                        invocation -> {
                            ((FutureCallback<SimpleHttpResponse>) invocation.getArgument(4))
                                    .cancelled();
                            return CompletableFuture.completedFuture(null);
                        });

        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.GET, "/test", Collections.emptyMap(), "thisisabody");
        Exception exception = assertThrows(Exception.class, () -> classToTest.get(request), "");
        assertThat(findRootCause(exception).getClass(), equalTo(CancellationException.class));
    }

    @Test
    void withException() {
        reset(client);
        when(client.execute(any(), any(), any(), any(), any()))
                .then(
                        invocation -> {
                            ((FutureCallback<SimpleHttpResponse>) invocation.getArgument(4))
                                    .failed(new Exception("BOOM!"));
                            return CompletableFuture.completedFuture(null);
                        });

        BasicHttpRequest request =
                new BasicHttpRequest(
                        HttpMethod.GET, "/test", Collections.emptyMap(), "thisisabody");
        Exception exception = assertThrows(Exception.class, () -> classToTest.get(request), "");
        assertThat(findRootCause(exception).getClass(), equalTo(Exception.class));
    }

    public Throwable findRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
