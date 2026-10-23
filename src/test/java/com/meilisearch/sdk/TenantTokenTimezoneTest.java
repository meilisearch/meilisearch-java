package com.meilisearch.sdk;

import static org.junit.jupiter.api.Assertions.*;

import com.auth0.jwt.JWT;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

class TenantTokenTimezoneTest {

    private final Client client =
            new Client(new Config("http://localhost:7700", "a-test-signing-key"));
    private final String apiKeyUid = "12345678-1234-4234-8234-123456789abc";

    @Test
    void preservesDefaultTimezoneAndTokenExpiration() throws Exception {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
            Date expiration = new Date((System.currentTimeMillis() / 1000 + 3600) * 1000);
            TenantTokenOptions options = new TenantTokenOptions();
            options.setExpiresAt(expiration);

            String token = client.generateTenantToken(apiKeyUid, Map.of("*", Map.of()), options);

            assertEquals("Asia/Shanghai", TimeZone.getDefault().getID());
            assertEquals(expiration, JWT.decode(token).getExpiresAt());
            assertEquals(apiKeyUid, JWT.decode(token).getClaim("apiKeyUid").asString());
        } finally {
            TimeZone.setDefault(original);
        }
    }

    @Test
    void preservesDefaultTimezoneWhenExpirationIsRejected() {
        TimeZone original = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            TenantTokenOptions options = new TenantTokenOptions();
            options.setExpiresAt(new Date(0));

            assertThrows(
                    MeilisearchException.class,
                    () -> client.generateTenantToken(apiKeyUid, Map.of("*", Map.of()), options));
            assertEquals("America/New_York", TimeZone.getDefault().getID());
        } finally {
            TimeZone.setDefault(original);
        }
    }
}
