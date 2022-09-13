package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.integration.classes.TestData;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.TenantTokenOptions;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.utils.Movie;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class TenantTokenTest extends AbstractIT {

    private TestData<Movie> testData;

    @BeforeEach
    public void initialize() {
        this.setUp();
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
    }

    /** Test Create Tenant Token with only search rules */
    @Test
    public void testGenerateTenantTokenWithOnlySearchRules() throws Exception {
        String indexUid = "BasicGenerateToken";
        Index index = createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        String jwtToken = privateClient.generateTenantToken(rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with search rules on one index */
    @Test
    public void testGenerateTenantTokenOnOneIndex() throws Exception {
        String indexUid = "GenerateTokenOnOneIndex";
        Index index = createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put(indexUid, new HashMap<String, Object>());

        String jwtToken = privateClient.generateTenantToken(rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with filter on search rules */
    @Test
    public void testGenerateTenantTokenWithFilter() throws Exception {
        Key key = getPrivateKey();
        String indexUid = "GenerateTokenwithFilter";

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("filter", "id > 1");
        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("GenerateTokenwithFilter", filters);

        String jwtToken = privateClient.generateTenantToken(rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        Index index = client.index(indexUid);
        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        Task task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getUid());

        Settings settings = index.getSettings();
        settings.setFilterableAttributes(new String[] {"id"});
        index.waitForTask(index.updateSettings(settings).getUid());

        SearchResult searchResult = tokenClient.index(indexUid).search("");

        assertEquals(20, searchResult.getHits().size());
        assertEquals(20, searchResult.getLimit());
        assertEquals(30, searchResult.getNbHits());
    }

    /** Test Create Tenant Token with expiration date */
    @Test
    public void testGenerateTenantTokenWithExpiresAt() throws Exception {
        String indexUid = "GenerateTokenWithExpiresAt";
        Index index = createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TenantTokenOptions options = new TenantTokenOptions();
        options.setExpiresAt(tomorrow);

        String jwtToken = privateClient.generateTenantToken(rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with api key */
    @Test
    public void testGenerateTenantTokenWithApiKey() throws Exception {
        String indexUid = "GenerateTokenWithApiKey";
        Index index = createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey(key.getKey());

        String jwtToken = client.generateTenantToken(rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with all options */
    @Test
    public void testGenerateTenantTokenWithAllOptions() throws Exception {
        String indexUid = "GenerateTokenWithAllOptions";
        Index index = createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey(key.getKey());
        options.setExpiresAt(tomorrow);

        String jwtToken = client.generateTenantToken(rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with no search rules */
    @Test
    public void testGenerateTenantTokenWithNoSearchRules() throws Exception {
        String indexUid = "GenerateTokenWithNoSearchRules";
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        assertThrows(MeilisearchException.class, () -> privateClient.generateTenantToken(null));
    }

    /** Test Create Tenant Token with bad expireation date */
    @Test
    public void testGenerateTenantTokenWithBadExpiresAt() throws Exception {
        String indexUid = "GenerateTenantTokenWithBadExpiresAt";
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        Date today = new Date();
        Date yesterday = new Date(today.getTime() - (1000 * 60 * 60 * 24));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        TenantTokenOptions options = new TenantTokenOptions();
        options.setExpiresAt(yesterday);

        assertThrows(
                MeilisearchException.class,
                () -> privateClient.generateTenantToken(rules, options));
    }

    /** Test Create Tenant Token with empty api key */
    @Test
    public void testGenerateTenantTokenWithEmptyApiKey() throws Exception {
        String indexUid = "GenerateTenantTokenWithEmptyApiKey";

        Client privateClient = new Client(new Config(getMeilisearchHost()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());
        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey("");

        assertThrows(
                MeilisearchException.class,
                () -> privateClient.generateTenantToken(rules, options));
    }
}
