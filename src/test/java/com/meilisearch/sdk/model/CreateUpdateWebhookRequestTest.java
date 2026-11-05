package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class CreateUpdateWebhookRequestTest {

    @Test
    void toStringIncludesUrlAndHeaders() {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");

        JSONObject json =
                new JSONObject(
                        new CreateUpdateWebhookRequest("http://example.com", headers).toString());

        assertThat(json.getString("url"), is(equalTo("http://example.com")));
        assertThat(json.getJSONObject("headers").getString("authorization"), is("MASTER_KEY"));
    }

    @Test
    void toStringKeepsNullHeaders() {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("referer", null);

        JSONObject json =
                new JSONObject(
                        new CreateUpdateWebhookRequest("http://example.com", headers).toString());

        assertThat(json.getJSONObject("headers").has("referer"), is(true));
        assertThat(json.getJSONObject("headers").isNull("referer"), is(true));
    }
}
