package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import com.meilisearch.sdk.http.CustomOkHttpClient;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.model.SwapIndexesParams;
import java.util.ArrayDeque;
import okhttp3.*;
import okhttp3.internal.connection.RealCall;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwapIndexesRenameTest {

    private final Config config = new Config("http://localhost:7700", "masterKey");
    private final OkHttpClient ok = mock(OkHttpClient.class);
    private final CustomOkHttpClient http = new CustomOkHttpClient(config, ok);

    private final ArrayDeque<Request> reqQ = new ArrayDeque<>();
    private final ArrayDeque<Response> resQ = new ArrayDeque<>();

    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @BeforeEach
    void setUp() throws Exception {
        RealCall call = mock(RealCall.class);

        when(ok.newCall(any()))
                .thenAnswer(
                        inv -> {
                            Request req = inv.getArgument(0);
                            reqQ.push(req);

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

                            resQ.push(resp);

                            return call;
                        });

        when(call.execute()).then(inv -> resQ.poll());
    }

    @Test
    void testSwapIndexesWithRename() throws Exception {
        SwapIndexesParams[] params = {
            new SwapIndexesParams().setIndexes(new String[] {"indexA", "indexB"}).setRename(true)
        };

        String json = "[{\"indexes\":[\"indexA\",\"indexB\"],\"rename\":true}]";

        HttpRequest req =
                new HttpRequest(HttpMethod.POST, "/swap-indexes", config.getHeaders(), json);

        http.post(req);

        Request okReq = reqQ.poll();

        assertThat(okReq.method(), equalTo("POST"));
        assertThat(okReq.url().toString(), equalTo("http://localhost:7700/swap-indexes"));
        assertThat(read(okReq.body()), containsString("\"rename\":true"));
    }

    private String read(RequestBody b) throws Exception {
        Buffer buf = new Buffer();
        b.writeTo(buf);
        return buf.readUtf8();
    }
}
