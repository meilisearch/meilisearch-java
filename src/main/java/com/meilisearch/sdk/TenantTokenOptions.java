package com.meilisearch.sdk;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/** The option you want to pass for generate a Tenant Token */
public class TenantTokenOptions {

    public TenantTokenOptions() {}

    @Getter @Setter protected String apiKey = null;
    @Getter @Setter protected Date expiresAt = null;
}
