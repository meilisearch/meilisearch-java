package com.meilisearch.sdk;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/** The option you want to pass for generate a Tenant Token */
@Getter
@Setter
@Accessors(chain = true)
public class TenantTokenOptions {

    public TenantTokenOptions() {}

    protected String apiKey = null;
    protected Date expiresAt = null;
}
