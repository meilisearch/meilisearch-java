package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.meilisearch.sdk.model.TaskInfo;
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

    @BeforeEach
    public void initialize() {
        this.setUp();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
    }

    /** Test Create Tenant Token with only search rules */
    @Test
    public void testGenerateTenantTokenWithOnlySearchRules() throws Exception {
        String indexUid = "BasicGenerateToken";
        createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<>();
        rules.put("*", new HashMap<String, Object>());

        String jwtToken = privateClient.generateTenantToken(getPrivateKey().getUid(), rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with search rules on one index */
    @Test
    public void testGenerateTenantTokenOnOneIndex() throws Exception {
        String indexUid = "GenerateTokenOnOneIndex";
        createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<>();
        rules.put(indexUid, new HashMap<String, Object>());

        String jwtToken = privateClient.generateTenantToken(getPrivateKey().getUid(), rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with filter on search rules */
    @Test
    public void testGenerateTenantTokenWithFilter() throws Exception {
        Key key = getPrivateKey();
        String indexUid = "GenerateTokenwithFilter";

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> filters = new HashMap<>();
        filters.put("filter", "id > 1");
        Map<String, Object> rules = new HashMap<>();
        rules.put("GenerateTokenwithFilter", filters);

        String jwtToken = privateClient.generateTenantToken(getPrivateKey().getUid(), rules);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        Index index = client.index(indexUid);
        TestData<Movie> testData = this.getTestData(MOVIES_INDEX, Movie.class);
        TaskInfo task = index.addDocuments(testData.getRaw());
        index.waitForTask(task.getTaskUid());

        Settings settings = index.getSettings();
        settings.setFilterableAttributes("id");
        index.waitForTask(index.updateSettings(settings).getTaskUid());

        SearchResult searchResult = tokenClient.index(indexUid).search("");

        assertThat(searchResult.getHits().size(), is(equalTo(20)));
        assertThat(searchResult.getLimit(), is(equalTo(20)));
        assertThat(searchResult.getEstimatedTotalHits(), is(equalTo(30)));
    }

    /** Test Create Tenant Token with expiration date */
    @Test
    public void testGenerateTenantTokenWithExpiresAt() throws Exception {
        String indexUid = "GenerateTokenWithExpiresAt";
        createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TenantTokenOptions options = new TenantTokenOptions();
        options.setExpiresAt(tomorrow);

        String jwtToken =
                privateClient.generateTenantToken(getPrivateKey().getUid(), rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with api key */
    @Test
    public void testGenerateTenantTokenWithApiKey() throws Exception {
        String indexUid = "GenerateTokenWithApiKey";
        createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey(key.getKey());

        String jwtToken = client.generateTenantToken(getPrivateKey().getUid(), rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with all options */
    @Test
    public void testGenerateTenantTokenWithAllOptions() throws Exception {
        String indexUid = "GenerateTokenWithAllOptions";
        createEmptyIndex(indexUid);
        Key key = getPrivateKey();

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey(key.getKey());
        options.setExpiresAt(tomorrow);

        String jwtToken = client.generateTenantToken(getPrivateKey().getUid(), rules, options);

        Client tokenClient = new Client(new Config(getMeilisearchHost(), jwtToken));

        assertDoesNotThrow(() -> tokenClient.index(indexUid).search(""));
    }

    /** Test Create Tenant Token with no search rules */
    @Test
    public void testGenerateTenantTokenWithNoSearchRules() throws Exception {
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        assertThrows(
                MeilisearchException.class,
                () -> privateClient.generateTenantToken(getPrivateKey().getUid(), null));
    }

    /** Test Create Tenant Token with no api key uid */
    @Test
    public void testGenerateTenantTokenWithNoApiKeyUid() throws Exception {
        Key key = getPrivateKey();

        Client privateClient = new Client(new Config(getMeilisearchHost(), key.getKey()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());

        assertThrows(
                MeilisearchException.class, () -> privateClient.generateTenantToken(null, rules));
    }

    /** Test Create Tenant Token with bad expireation date */
    @Test
    public void testGenerateTenantTokenWithBadExpiresAt() throws Exception {
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
                () -> privateClient.generateTenantToken(getPrivateKey().getUid(), rules, options));
    }

    /** Test Create Tenant Token with empty api key */
    @Test
    public void testGenerateTenantTokenWithEmptyApiKey() throws Exception {
        Client privateClient = new Client(new Config(getMeilisearchHost()));

        Map<String, Object> rules = new HashMap<String, Object>();
        rules.put("*", new HashMap<String, Object>());
        TenantTokenOptions options = new TenantTokenOptions();
        options.setApiKey("");

        assertThrows(
                MeilisearchException.class,
                () -> privateClient.generateTenantToken(getPrivateKey().getUid(), rules, options));
    }
}
