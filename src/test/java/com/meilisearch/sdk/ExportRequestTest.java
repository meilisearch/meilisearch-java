package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class ExportRequestTest {

    @Test
    void toStringSimpleExportIndexFilter() {
        ExportIndexFilter filter = ExportIndexFilter.builder().build();
        String expected = "{\"overrideSettings\":false}";
        assertThat(filter.toString(), is(equalTo(expected)));
        assertThat(filter.getFilter(), is(nullValue()));
        assertThat(filter.isOverrideSettings(), is(false));
    }

    @Test
    void toStringExportIndexFilterWithOverride() {
        ExportIndexFilter filter = ExportIndexFilter.builder().overrideSettings(true).build();
        String expected = "{\"overrideSettings\":true}";
        assertThat(filter.toString(), is(equalTo(expected)));
        assertThat(filter.isOverrideSettings(), is(true));
    }

    @Test
    void toStringExportIndexFilterWithFilter() {
        ExportIndexFilter filter = ExportIndexFilter.builder().filter("status = 'active'").build();
        String expected = "{\"filter\":\"status = 'active'\",\"overrideSettings\":false}";
        assertThat(filter.toString(), is(equalTo(expected)));
        assertThat(filter.getFilter(), is(equalTo("status = 'active'")));
    }

    @Test
    void toStringSimpleExportRequest() {
        ExportRequest request =
                ExportRequest.builder().url("http://localhost:7711").payloadSize("123 MiB").build();
        JSONObject json = new JSONObject(request.toString());
        assertThat(json.getString("url"), is(equalTo("http://localhost:7711")));
        assertThat(json.getString("payloadSize"), is(equalTo("123 MiB")));
        assertThat(json.isNull("apiKey"), is(true));
        assertThat(json.isNull("indexes"), is(true));
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

        String expected =
                "{\"url\":\"http://localhost:7711\",\"payloadSize\":\"123 MiB\",\"indexes\":{\"*\":{\"overrideSettings\":true}}}";
        JSONObject expectedJson = new JSONObject(expected);
        JSONObject json = new JSONObject(request.toString());

        assertThat(expectedJson.toString(), is(json.toString()));

        assertThat(json.getString("url"), is(equalTo("http://localhost:7711")));
        assertThat(json.getString("payloadSize"), is(equalTo("123 MiB")));
        assertThat(json.isNull("apiKey"), is(true));
        JSONObject indexesJson = json.getJSONObject("indexes");
        JSONObject starIndex = indexesJson.getJSONObject("*");
        assertThat(starIndex.isNull("filter"), is(true));
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
