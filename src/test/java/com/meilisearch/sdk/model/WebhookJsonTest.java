package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import com.meilisearch.sdk.json.JsonHandler;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class WebhookJsonTest {

    private static final String UUID_VALUE = "627ea538-733d-4545-8d2d-03526eb381ce";
    private static final String WEBHOOK_JSON =
            "{\"uuid\":\""
                    + UUID_VALUE
                    + "\",\"url\":\"http://example.com\","
                    + "\"headers\":{\"authorization\":\"MASTER_KEY\"},\"isEditable\":true}";

    static Stream<JsonHandler> handlers() {
        return Stream.of(new GsonJsonHandler(), new JacksonJsonHandler());
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void decodesWebhook(JsonHandler handler) throws Exception {
        Webhook webhook = handler.decode(WEBHOOK_JSON, Webhook.class);

        assertThat(webhook.getUuid(), is(equalTo(UUID.fromString(UUID_VALUE))));
        assertThat(webhook.getUrl(), is(equalTo("http://example.com")));
        assertThat(webhook.getHeaders(), is(aMapWithSize(1)));
        assertThat(webhook.getHeaders(), hasEntry("authorization", "MASTER_KEY"));
        assertThat(webhook.isEditable(), is(true));
    }

    @ParameterizedTest
    @MethodSource("handlers")
    @SuppressWarnings("unchecked")
    void decodesWebhookResults(JsonHandler handler) throws Exception {
        Results<Webhook> results =
                handler.decode(
                        "{\"results\":[" + WEBHOOK_JSON + "]}", Results.class, Webhook.class);

        assertThat(results.getResults(), is(arrayWithSize(1)));
        assertThat(results.getResults()[0].getUuid(), is(equalTo(UUID.fromString(UUID_VALUE))));
        assertThat(results.getResults()[0].isEditable(), is(true));
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void encodesWebhookWithApiFieldNames(JsonHandler handler) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        Webhook webhook =
                new Webhook(UUID.fromString(UUID_VALUE), "http://example.com", headers, true);

        JSONObject json = new JSONObject(handler.encode(webhook));

        assertThat(json.similar(new JSONObject(WEBHOOK_JSON)), is(true));
    }

    @ParameterizedTest
    @MethodSource("handlers")
    void encodesRequest(JsonHandler handler) throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");

        JSONObject json =
                new JSONObject(
                        handler.encode(
                                new CreateUpdateWebhookRequest("http://example.com", headers)));

        assertThat(json.getString("url"), is(equalTo("http://example.com")));
        assertThat(json.getJSONObject("headers").getString("authorization"), is("MASTER_KEY"));
    }

    @Test
    void jacksonKeepsNullHeadersInRequest() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("referer", null);

        JSONObject json =
                new JSONObject(
                        new JacksonJsonHandler()
                                .encode(
                                        new CreateUpdateWebhookRequest(
                                                "http://example.com", headers)));

        assertThat(json.getJSONObject("headers").has("referer"), is(true));
        assertThat(json.getJSONObject("headers").isNull("referer"), is(true));
    }
}
