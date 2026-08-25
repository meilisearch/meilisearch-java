package com.meilisearch.sdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.meilisearch.sdk.http.request.BasicRequest;
import com.meilisearch.sdk.http.request.HttpMethod;
import com.meilisearch.sdk.http.request.HttpRequest;
import com.meilisearch.sdk.json.GsonJsonHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class RenderTemplateRequestTest {
    @Test
    void toStringWithTemplateAndInput() {
        Map<String, Object> template = new HashMap<>();
        template.put("kind", "inlineDocumentTemplate");
        template.put("inline", "Product {{doc.name}} is a {{doc.color}} {{doc.category}}.");

        Map<String, Object> product = new HashMap<>();
        product.put("name", "Nike Air Max");
        product.put("color", "Black");
        product.put("category", "Shoes");

        Map<String, Object> input = new HashMap<>();
        input.put("kind", "inlineDocument");
        input.put("inline", product);

        RenderTemplateRequest request =
                new RenderTemplateRequest().setTemplate(template).setInput(input);

        JSONObject json = new JSONObject(request.toString());

        assertThat(
                json.getJSONObject("template").getString("kind"),
                is(equalTo("inlineDocumentTemplate")));
        assertThat(
                json.getJSONObject("template").getString("inline"),
                is(equalTo("Product {{doc.name}} is a {{doc.color}} {{doc.category}}.")));

        assertThat(json.getJSONObject("input").getString("kind"), is(equalTo("inlineDocument")));
        assertThat(
                json.getJSONObject("input").getJSONObject("inline").getString("name"),
                is(equalTo("Nike Air Max")));
        assertThat(
                json.getJSONObject("input").getJSONObject("inline").getString("color"),
                is(equalTo("Black")));
        assertThat(
                json.getJSONObject("input").getJSONObject("inline").getString("category"),
                is(equalTo("Shoes")));
    }

    @Test
    void toStringWithoutInput() {
        Map<String, Object> template = new HashMap<>();
        template.put("kind", "inlineDocumentTemplate");
        template.put("inline", "Hello {{doc.name}}");

        RenderTemplateRequest request = new RenderTemplateRequest().setTemplate(template);

        JSONObject json = new JSONObject(request.toString());

        assertThat(json.has("template"), is(true));
        assertThat(json.has("input"), is(false));
        assertThat(
                json.getJSONObject("template").getString("kind"),
                is(equalTo("inlineDocumentTemplate")));
        assertThat(
                json.getJSONObject("template").getString("inline"),
                is(equalTo("Hello {{doc.name}}")));
    }

    @Test
    void requestSerializerWithTemplateAndInput() {
        Map<String, Object> template = new HashMap<>();
        template.put("kind", "inlineDocumentTemplate");
        template.put("inline", "Product {{doc.name}} is a {{doc.color}} {{doc.category}}.");

        Map<String, Object> product = new HashMap<>();
        product.put("name", "Nike Air Max");
        product.put("color", "Black");
        product.put("category", "Shoes");

        Map<String, Object> input = new HashMap<>();
        input.put("kind", "inlineDocument");
        input.put("inline", product);

        RenderTemplateRequest request =
                new RenderTemplateRequest().setTemplate(template).setInput(input);

        BasicRequest basicRequest = new BasicRequest(new GsonJsonHandler());
        HttpRequest httpRequest =
                basicRequest.create(
                        HttpMethod.POST, "/render-template", Collections.emptyMap(), request);

        JSONObject json = new JSONObject(httpRequest.getContent());

        assertThat(httpRequest.getMethod(), is(equalTo(HttpMethod.POST)));
        assertThat(httpRequest.getPath(), is(equalTo("/render-template")));
        assertThat(
                json.getJSONObject("template").getString("kind"),
                is(equalTo("inlineDocumentTemplate")));
        assertThat(json.getJSONObject("input").getString("kind"), is(equalTo("inlineDocument")));
        assertThat(
                json.getJSONObject("input").getJSONObject("inline").getString("name"),
                is(equalTo("Nike Air Max")));
    }

    @Test
    void requestSerializerWithoutInput() {
        Map<String, Object> template = new HashMap<>();
        template.put("kind", "inlineDocumentTemplate");
        template.put("inline", "Hello {{doc.name}}");

        RenderTemplateRequest request = new RenderTemplateRequest().setTemplate(template);

        BasicRequest basicRequest = new BasicRequest(new GsonJsonHandler());
        HttpRequest httpRequest =
                basicRequest.create(
                        HttpMethod.POST, "/render-template", Collections.emptyMap(), request);

        JSONObject json = new JSONObject(httpRequest.getContent());

        assertThat(httpRequest.getMethod(), is(equalTo(HttpMethod.POST)));
        assertThat(httpRequest.getPath(), is(equalTo("/render-template")));
        assertThat(json.has("template"), is(true));
        assertThat(json.has("input"), is(false));
    }

    @Test
    void gettersAndSetters() {
        Map<String, Object> template = new HashMap<>();
        template.put("kind", "inlineDocumentTemplate");

        Map<String, Object> input = new HashMap<>();
        input.put("kind", "inlineDocument");

        RenderTemplateRequest request =
                RenderTemplateRequest.builder().template(template).input(input).build();

        assertThat(request.getTemplate(), is(equalTo(template)));
        assertThat(request.getInput(), is(equalTo(input)));
    }
}
