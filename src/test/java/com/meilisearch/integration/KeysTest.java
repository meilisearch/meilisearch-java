package com.meilisearch.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.Key;
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
    }

    @AfterAll
    static void cleanMeiliSearch() {
        cleanup();
        deleteAllKeys();
    }

    /** Test Get Keys */
    @Test
    public void testClientGetKeys() throws Exception {
        Key[] keys = client.getKeys();

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
