package com.meilisearch.sdk;

import java.util.Date;

/** The option you want to pass for generate a Tenant Token */
public class TenantTokenOptions {

    public TenantTokenOptions() {}

    protected String apiKey = null;
    protected Date expiresAt = null;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
