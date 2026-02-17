package com.meilisearch.sdk;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.http.URLBuilder;
import com.meilisearch.sdk.model.CreateUpdateWebhookRequest;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Webhook;
import java.util.UUID;

public class WebHooksHandler {
    private final HttpClient httpClient;

    protected WebHooksHandler(Config config) {
        this.httpClient = config.httpClient;
    }

    /**
     * Gets a list of webhooks
     *
     * @return List of webhooks.
     * @throws MeilisearchException if an error occurs
     */
    Results<Webhook> getWebhooks() throws MeilisearchException {
        return this.httpClient.get(webhooksPath().getURL(), Results.class, Webhook.class);
    }

    /**
     * Gets a webhook from its uuid
     *
     * @param uuid Unique identifier of the webhook to get
     * @return Meilisearch API response as Webhook instance
     * @throws MeilisearchException if an error occurs
     */
    Webhook getWebhook(UUID uuid) throws MeilisearchException {
        return this.httpClient.get(
                webhooksPath().addSubroute(uuid.toString()).getURL(), Webhook.class);
    }

    /**
     * Create a new webhook
     *
     * @param createUpdateWebhookRequest Request body for creating a new webhook
     * @return Meilisearch API response as Webhook instance
     * @throws MeilisearchException if an error occurs
     */
    Webhook createWebhook(CreateUpdateWebhookRequest createUpdateWebhookRequest)
            throws MeilisearchException {
        return this.httpClient.post(
                webhooksPath().getURL(), createUpdateWebhookRequest, Webhook.class);
    }

    /**
     * Update a webhook
     *
     * @param webhookUuid Unique identifier of a webhook to update
     * @param createUpdateWebhookRequest Request body for updating a webhook
     * @return Meilisearch API response as Webhook instance
     * @throws MeilisearchException if an error occurs
     */
    Webhook updateWebhook(UUID webhookUuid, CreateUpdateWebhookRequest createUpdateWebhookRequest)
            throws MeilisearchException {
        return this.httpClient.patch(
                webhooksPath().addSubroute(webhookUuid.toString()).getURL(),
                createUpdateWebhookRequest,
                Webhook.class);
    }

    /**
     * Delete a webhook
     *
     * @param webhookUuid Unique identifier of a webhook to update
     * @throws MeilisearchException if an error occurs
     */
    void deleteWebhook(UUID webhookUuid) throws MeilisearchException {
        this.httpClient.delete(
                webhooksPath().addSubroute(webhookUuid.toString()).getURL(), String.class);
    }

    private URLBuilder webhooksPath() {
        return new URLBuilder("/webhooks");
    }
}
