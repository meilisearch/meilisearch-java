package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import com.meilisearch.sdk.http.CustomOkHttpClient;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.http.response.HttpResponse;
import java.util.ArrayDeque;
import okhttp3.*;
import okhttp3.internal.connection.RealCall;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexRenameTest {

    private final Config config = new Config("http://localhost:7700", "masterKey");
    private final OkHttpClient ok = mock(OkHttpClient.class);
    private final CustomOkHttpClient http = new CustomOkHttpClient(config, ok);

    private final ArrayDeque<Request> requestQueue = new ArrayDeque<>();
    private final ArrayDeque<Response> responseQueue = new ArrayDeque<>();

    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @BeforeEach
    void setUp() throws Exception {
        RealCall call = mock(RealCall.class);

        when(ok.newCall(any()))
                .thenAnswer(
                        inv -> {
                            Request req = inv.getArgument(0);
                            requestQueue.push(req);

                            Response resp =
                                    new Response.Builder()
                                            .request(req)
                                            .protocol(Protocol.HTTP_1_1)
                                            .code(200)
                                            .message("OK")
                                            .body(
                                                    ResponseBody.create(
                                                            "{\"status\":\"enqueued\"}", JSON))
                                            .build();

                            responseQueue.push(resp);
                            return call;
                        });

        when(call.execute()).then(inv -> responseQueue.poll());
    }

    @Test
    void testRenameIndex() throws Exception {
        // Build the PATCH request the same way IndexesHandler does:
        String body = "{\"indexUid\":\"indexB\"}";
        HttpRequest request =
                new HttpRequest(HttpMethod.PATCH, "/indexes/indexA", config.getHeaders(), body);

        HttpResponse<Object> resp = http.patch(request);

        assertThat(resp.getStatusCode(), equalTo(200));

        Request okReq = requestQueue.poll();

        assertThat(okReq.method(), equalTo("PATCH"));
        assertThat(okReq.url().toString(), equalTo("http://localhost:7700/indexes/indexA"));
        assertThat(read(okReq.body()), containsString("\"indexUid\":\"indexB\""));
    }

    private String read(RequestBody body) throws Exception {
        Buffer buf = new Buffer();
        body.writeTo(buf);
        return buf.readUtf8();
    }
}
