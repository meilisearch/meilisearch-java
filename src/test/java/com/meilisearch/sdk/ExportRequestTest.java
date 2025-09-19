package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class ExportRequestTest {

    @Test
    void toStringSimpleExportIndexFilter() {
        ExportIndexFilter filter = ExportIndexFilter.builder().build();
        JSONObject json = new JSONObject(filter.toString());
        assertThat(json.has("overrideSettings"), is(false));
        assertThat(json.has("filter"), is(false));
    }

    @Test
    void toStringExportIndexFilterWithOverride() {
        ExportIndexFilter filter = ExportIndexFilter.builder().overrideSettings(true).build();
        JSONObject json = new JSONObject(filter.toString());
        assertThat(json.getBoolean("overrideSettings"), is(true));
        assertThat(json.has("filter"), is(false));
    }

    @Test
    void toStringExportIndexFilterWithFilter() {
        ExportIndexFilter filter = ExportIndexFilter.builder().filter("status = 'active'").build();
        JSONObject json = new JSONObject(filter.toString());
        assertThat(json.getString("filter"), is(equalTo("status = 'active'")));
        assertThat(json.has("overrideSettings"), is(false));
    }

    @Test
    void toStringSimpleExportRequest() {
        ExportRequest request =
                ExportRequest.builder().url("http://localhost:7711").payloadSize("123 MiB").build();
        JSONObject json = new JSONObject(request.toString());
        assertThat(json.getString("url"), is(equalTo("http://localhost:7711")));
        assertThat(json.getString("payloadSize"), is(equalTo("123 MiB")));
        assertThat(json.has("apiKey"), is(false));
        assertThat(json.has("indexes"), is(false));
    }

    @Test
    void toStringExportRequestWithIndexes() {
        Map<String, ExportIndexFilter> indexes = new HashMap<>();
        indexes.put("*", ExportIndexFilter.builder().overrideSettings(true).build());

        ExportRequest request =
                ExportRequest.builder()
                        .url("http://localhost:7711")
                        .payloadSize("123 MiB")
                        .indexes(indexes)
                        .build();

        JSONObject json = new JSONObject(request.toString());

        assertThat(json.getString("url"), is(equalTo("http://localhost:7711")));
        assertThat(json.getString("payloadSize"), is(equalTo("123 MiB")));
        assertThat(json.has("apiKey"), is(false));
        JSONObject indexesJson = json.getJSONObject("indexes");
        JSONObject starIndex = indexesJson.getJSONObject("*");
        assertThat(starIndex.has("filter"), is(false));
        assertThat(starIndex.getBoolean("overrideSettings"), is(true));
    }

    @Test
    void gettersExportRequest() {
        Map<String, ExportIndexFilter> indexes = new HashMap<>();
        indexes.put(
                "myindex",
                ExportIndexFilter.builder().filter("id > 10").overrideSettings(false).build());

        ExportRequest request =
                ExportRequest.builder()
                        .url("http://localhost:7711")
                        .apiKey("mykey")
                        .payloadSize("50 MiB")
                        .indexes(indexes)
                        .build();

        assertThat(request.getUrl(), is(equalTo("http://localhost:7711")));
        assertThat(request.getApiKey(), is(equalTo("mykey")));
        assertThat(request.getPayloadSize(), is(equalTo("50 MiB")));
        assertThat(request.getIndexes().get("myindex").getFilter(), is(equalTo("id > 10")));
        assertThat(request.getIndexes().get("myindex").isOverrideSettings(), is(false));
    }
}
