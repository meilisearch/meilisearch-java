package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.model.CreateUpdateWebhookRequest;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Webhook;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class WebhooksTest extends AbstractIT {

    @BeforeEach
    public void initialize() {
        this.setUp();
        this.setUpJacksonClient();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    void cleanUp() {
        Results<Webhook> webhooks = client.getWebhooks();
        for (Webhook wb : webhooks.getResults()) {
            client.deleteWebhook(wb.getUuid());
        }
    }

    @Test
    public void testCreateWebhook() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        headers.put("referer", "http://example.com");
        CreateUpdateWebhookRequest webhookReq1 =
                new CreateUpdateWebhookRequest("http://webiste.com", headers);

        Webhook webhook = this.client.createWebhook(webhookReq1);

        assertThat(webhook, is(instanceOf(Webhook.class)));
        assertThat(webhook.getUuid(), is(notNullValue()));
        assertThat(webhook.getHeaders(), is(notNullValue()));
        assertThat(webhook.getUrl(), is(notNullValue()));
        assertThat(webhook.isEditable(), is(notNullValue()));
        cleanUp();
    }

    @Test
    public void testGetWebhooks() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        headers.put("referer", "http://example.com");
        CreateUpdateWebhookRequest webhookReq1 =
                new CreateUpdateWebhookRequest("http://webiste.com", headers);
        CreateUpdateWebhookRequest webhookReq2 =
                new CreateUpdateWebhookRequest("http://webiste1.com", headers);

        this.client.createWebhook(webhookReq1);
        this.client.createWebhook(webhookReq2);

        Results<Webhook> webhooks = client.getWebhooks();
        Webhook webhook = webhooks.getResults()[0];
        Webhook webhook1 = webhooks.getResults()[1];

        assertThat(webhook, is(instanceOf(Webhook.class)));
        assertThat(webhook.getUuid(), is(notNullValue()));
        assertThat(webhook.getHeaders(), is(notNullValue()));
        assertThat(webhook.getUrl(), is(notNullValue()));
        assertThat(webhook.isEditable(), is(notNullValue()));

        assertThat(webhook1, is(instanceOf(Webhook.class)));
        assertThat(webhook1.getUuid(), is(notNullValue()));
        assertThat(webhook1.getHeaders(), is(notNullValue()));
        assertThat(webhook1.getUrl(), is(notNullValue()));
        assertThat(webhook1.isEditable(), is(notNullValue()));
        cleanUp();
    }

    @Test
    public void testGetWebhook() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        headers.put("referer", "http://example.com");
        CreateUpdateWebhookRequest webhookReq1 =
                new CreateUpdateWebhookRequest("http://webiste.com", headers);

        Webhook webhook = this.client.createWebhook(webhookReq1);

        Webhook webhook_res = client.getWebhook(webhook.getUuid());

        assertThat(webhook_res, is(instanceOf(Webhook.class)));
        assertThat(webhook_res.getUuid(), is(notNullValue()));
        assertThat(webhook_res.getHeaders(), is(notNullValue()));
        assertThat(webhook_res.getUrl(), is(notNullValue()));
        assertThat(webhook_res.isEditable(), is(notNullValue()));
        cleanUp();
    }

    @Test
    public void testUpdateWebhook() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        headers.put("referer", "http://example.com");
        CreateUpdateWebhookRequest webhookReq1 =
                new CreateUpdateWebhookRequest("http://webiste.com", headers);
        Webhook webhook = this.client.createWebhook(webhookReq1);

        headers.put("referer", null);
        CreateUpdateWebhookRequest webhookReq2 =
                new CreateUpdateWebhookRequest(webhook.getUrl(), headers);

        Webhook updated_webhook = this.client.updateWebhook(webhook.getUuid(), webhookReq2);

        assertThat(updated_webhook, is(instanceOf(Webhook.class)));
        assertThat(updated_webhook.getUuid(), is(notNullValue()));
        assertThat(updated_webhook.getHeaders(), is(notNullValue()));
        assertThat(updated_webhook.getUrl(), is(notNullValue()));
        assertThat(updated_webhook.isEditable(), is(notNullValue()));
        assertThat(updated_webhook.getHeaders(), is(aMapWithSize(1)));
        cleanUp();
    }

    @Test
    public void testDeleteWebhook() throws Exception {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("authorization", "MASTER_KEY");
        headers.put("referer", "http://example.com");
        CreateUpdateWebhookRequest webhookReq1 =
                new CreateUpdateWebhookRequest("http://webiste.com", headers);
        Webhook webhook = this.client.createWebhook(webhookReq1);

        this.client.deleteWebhook(webhook.getUuid());

        List<Webhook> webhooks = Arrays.asList(this.client.getWebhooks().getResults());

        assertThat(webhooks, hasSize(0));
        cleanUp();
    }
}
