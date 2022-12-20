package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.Results;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class KeysTest extends AbstractIT {

    @BeforeEach
    public void initialize() {
        this.setUp();
        this.setUpJacksonClient();
    }

    @AfterAll
    static void cleanMeilisearch() {
        cleanup();
        deleteAllKeys();
    }

    /** Test Get Keys */
    @Test
    public void testClientGetKeys() throws Exception {
        Results<Key> result = client.getKeys();
        Key[] keys = result.getResults();

        assertEquals(2, keys.length);

        for (Key key : keys) {
            assertNotNull(key.getKey());
            assertNotNull(key.getActions());
            assertNotNull(key.getIndexes());
            assertNotNull(key.getDescription());
            assertNull(key.getExpiresAt());
            assertNotNull(key.getCreatedAt());
            assertNotNull(key.getUpdatedAt());
        }
    }

    /** Test Get Keys with Jackson Json Handler */
    @Test
    public void testClientGetKeysWithJacksonJsonHandler() throws Exception {
        Results<Key> result = clientJackson.getKeys();
        Key[] keys = result.getResults();

        assertEquals(5, keys.length);

        for (Key key : keys) {
            assertNotNull(key.getKey());
            assertNotNull(key.getActions());
            assertNotNull(key.getIndexes());
            assertNotNull(key.getCreatedAt());
            assertNotNull(key.getUpdatedAt());
        }
    }

    /** Test Get Key */
    @Test
    public void testClientGetKey() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key createKey = client.createKey(keyInfo);
        Key key = client.getKey(createKey.getKey());

        assertTrue(key instanceof Key);
        assertNotNull(key.getKey());
        assertNotNull(key.getActions());
        assertNotNull(key.getIndexes());
        assertNull(key.getDescription());
        assertNull(key.getExpiresAt());
        assertNotNull(key.getCreatedAt());
        assertNotNull(key.getUpdatedAt());
    }

    /** Test Create a simple API Key without description */
    @Test
    public void testClientCreateDefaultKey() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key key = client.createKey(keyInfo);

        assertTrue(key instanceof Key);
        assertNotNull(key.getKey());
        assertEquals("*", key.getActions()[0]);
        assertEquals("*", key.getIndexes()[0]);
        assertNull(key.getDescription());
        assertNull(key.getExpiresAt());
        assertNotNull(key.getCreatedAt());
        assertNotNull(key.getUpdatedAt());
    }

    /** Test Create an API Key with description */
    @Test
    public void testClientCreateKeyWithDescription() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setDescription("testClientCreateKey");
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key key = client.createKey(keyInfo);

        assertTrue(key instanceof Key);
        assertNotNull(key.getKey());
        assertEquals("*", key.getActions()[0]);
        assertEquals("*", key.getIndexes()[0]);
        assertEquals("testClientCreateKey", key.getDescription());
        assertNull(key.getExpiresAt());
        assertNotNull(key.getCreatedAt());
        assertNotNull(key.getUpdatedAt());
    }

    /** Test Create an API Key with expiresAt */
    @Test
    public void testClientCreateKeyWithExpirationDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateParsed = format.parse("2042-01-30");

        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(dateParsed);

        Key key = client.createKey(keyInfo);

        assertTrue(key instanceof Key);
        assertNotNull(key.getKey());
        assertEquals("*", key.getActions()[0]);
        assertEquals("*", key.getIndexes()[0]);
        assertNull(key.getDescription());
        assertEquals("2042-01-30", format.format(key.getExpiresAt()));
        assertNotNull(key.getCreatedAt());
        assertNotNull(key.getUpdatedAt());
    }

    /** Test Update an API Key */
    @Test
    public void testClientUpdateKey() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateParsed = format.parse("2042-01-30T00:00:00Z");

        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});

        Key keyChanges = new Key();
        keyChanges.setIndexes(new String[] {"testUpdateKey"});
        keyChanges.setActions(new String[] {"search"});
        keyChanges.setExpiresAt(dateParsed);

        Key createKey = client.createKey(keyInfo);
        Key updateKey = client.updateKey(createKey.getKey(), keyChanges);

        assertTrue(createKey instanceof Key);
        assertTrue(updateKey instanceof Key);
        assertNotNull(updateKey.getKey());
        assertEquals("*", createKey.getActions()[0]);
        assertEquals("search", updateKey.getActions()[0]);
        assertEquals("*", createKey.getIndexes()[0]);
        assertEquals("testUpdateKey", updateKey.getIndexes()[0]);
        assertNull(createKey.getExpiresAt());
        assertEquals(dateParsed, updateKey.getExpiresAt());
        assertNotNull(updateKey.getCreatedAt());
        assertNotNull(updateKey.getUpdatedAt());
    }

    /** Test Delete an API Key */
    @Test
    public void testClientDeleteKey() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key createKey = client.createKey(keyInfo);
        client.deleteKey(createKey.getKey());

        assertThrows(Exception.class, () -> client.getKey(createKey.getKey()));
    }
}
