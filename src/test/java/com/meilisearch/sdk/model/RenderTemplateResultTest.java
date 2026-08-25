package com.meilisearch.sdk.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.json.JacksonJsonHandler;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RenderTemplateResultTest {
    @Test
    void deserializesWithJacksonJsonHandler() throws Exception {
        RenderTemplateResult result =
                new JacksonJsonHandler()
                        .decode(
                                "{\"template\":\"Hello {{doc.name}}\",\"rendered\":\"Hello Movindu\"}",
                                RenderTemplateResult.class);

        assertThat(result.getTemplate(), is(equalTo("Hello {{doc.name}}")));
        assertThat(result.getRendered(), is(equalTo("Hello Movindu")));
    }

    @Test
    void deserializesStructuredRenderedValueWithJacksonJsonHandler() throws Exception {
        RenderTemplateResult result =
                new JacksonJsonHandler()
                        .decode(
                                "{"
                                        + "\"template\":\"{{ doc }}\","
                                        + "\"rendered\":{"
                                        + "\"title\":\"Ariel\","
                                        + "\"tags\":[\"movie\",\"classic\"]"
                                        + "}"
                                        + "}",
                                RenderTemplateResult.class);

        assertThat(result.getTemplate(), is(equalTo("{{ doc }}")));

        Map<?, ?> rendered = (Map<?, ?>) result.getRendered();
        assertThat(rendered.get("title"), is(equalTo("Ariel")));

        List<?> tags = (List<?>) rendered.get("tags");
        assertThat(tags.get(0), is(equalTo("movie")));
        assertThat(tags.get(1), is(equalTo("classic")));
    }
}
