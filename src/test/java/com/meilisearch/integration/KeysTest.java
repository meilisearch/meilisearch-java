package com.meilisearch.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.Key;
import com.meilisearch.sdk.model.KeyUpdate;
import com.meilisearch.sdk.model.KeysQuery;
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
        cleanup();
        deleteAllKeys();
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

        assertThat(keys, is(arrayWithSize(4)));

        for (Key key : keys) {
            assertThat(key.getKey(), is(notNullValue()));
            assertThat(key.getUid(), is(notNullValue()));
            assertThat(key.getActions(), is(notNullValue()));
            assertThat(key.getIndexes(), is(notNullValue()));
            assertThat(key.getDescription(), is(notNullValue()));
            assertThat(key.getExpiresAt(), is(nullValue()));
            assertThat(key.getCreatedAt(), is(notNullValue()));
            assertThat(key.getUpdatedAt(), is(notNullValue()));
        }
    }

    /** Test Get Keys with Jackson Json Handler */
    @Test
    public void testClientGetKeysWithJacksonJsonHandler() throws Exception {
        Results<Key> result = clientJackson.getKeys();
        Key[] keys = result.getResults();

        assertThat(keys, is(arrayWithSize(4)));

        for (Key key : keys) {
            assertThat(key.getKey(), is(notNullValue()));
            assertThat(key.getActions(), is(notNullValue()));
            assertThat(key.getIndexes(), is(notNullValue()));
            assertThat(key.getCreatedAt(), is(notNullValue()));
            assertThat(key.getUpdatedAt(), is(notNullValue()));
        }
    }

    /** Test Get Keys With Limit */
    @Test
    public void testClientGetKeysLimit() throws Exception {
        int limit = 24;
        KeysQuery query = new KeysQuery().setLimit(limit);

        Results<Key> result = client.getKeys(query);

        assertThat(result.getLimit(), is(equalTo(limit)));
        assertThat(result.getOffset(), is(notNullValue()));
        assertThat(result.getTotal(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Keys With Limit and Offset */
    @Test
    public void testClientGetKeysLimitAndOffset() throws Exception {
        int limit = 24;
        int offset = 2;
        KeysQuery query = new KeysQuery().setLimit(limit).setOffset(offset);

        Results<Key> result = client.getKeys(query);

        assertThat(result.getLimit(), is(equalTo(limit)));
        assertThat(result.getOffset(), is(equalTo(offset)));
        assertThat(result.getTotal(), is(notNullValue()));
        assertThat(result.getResults().length, is(notNullValue()));
    }

    /** Test Get Key */
    @Test
    public void testClientGetKey() throws Exception {
        Results<Key> result = client.getKeys();
        Key[] keys = result.getResults();

        Key key = client.getKey(keys[0].getKey());

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions(), is(notNullValue()));
        assertThat(key.getIndexes(), is(notNullValue()));
        assertThat(key.getDescription(), is(notNullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Get Key With Uid */
    @Test
    public void testClientGetKeyWithUid() throws Exception {
        Results<Key> result = client.getKeys();
        Key[] keys = result.getResults();

        Key key = client.getKey(keys[0].getUid());

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions(), is(notNullValue()));
        assertThat(key.getIndexes(), is(notNullValue()));
        assertThat(key.getDescription(), is(notNullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Get Key when the key does not exist */
    @Test
    public void testClientGetKeyDoesNotExist() throws Exception {
        assertThrows(MeilisearchApiException.class, () -> client.getKey("KeyDoesNotExist"));
    }

    /** Test Create a simple API Key without description */
    @Test
    public void testClientCreateDefaultKey() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key key = client.createKey(keyInfo);

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions()[0], is(equalTo("*")));
        assertThat(key.getIndexes()[0], is(equalTo("*")));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Create a simple API Key without description with Jackson Json Handler */
    @Test
    public void testClientCreateDefaultKeyWithJacksonJsonHandler() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key key = clientJackson.createKey(keyInfo);

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions()[0], is(equalTo("*")));
        assertThat(key.getIndexes()[0], is(equalTo("*")));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
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

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions()[0], is(equalTo("*")));
        assertThat(key.getIndexes()[0], is(equalTo("*")));
        assertThat(key.getDescription(), is(equalTo("testClientCreateKey")));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
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

        assertThat(key, is(instanceOf(Key.class)));
        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions()[0], is(equalTo("*")));
        assertThat(key.getIndexes()[0], is(equalTo("*")));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(format.format(key.getExpiresAt()), is(equalTo("2042-01-30")));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Create an API Key with wildcarded action */
    @Test
    public void testClientCreateKeyWithWilcardedAction() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"documents.*"});
        keyInfo.setExpiresAt(null);

        Key key = client.createKey(keyInfo);

        assertThat(key.getKey(), is(notNullValue()));
        assertThat(key.getActions()[0], is(equalTo("documents.*")));
        assertThat(key.getIndexes()[0], is(equalTo("*")));
        assertThat(key.getDescription(), is(nullValue()));
        assertThat(key.getExpiresAt(), is(nullValue()));
        assertThat(key.getCreatedAt(), is(notNullValue()));
        assertThat(key.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Update an API Key */
    @Test
    public void testClientUpdateKey() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateParsed = format.parse("2042-01-30T00:00:00Z");

        Key keyInfo = new Key();
        keyInfo.setName("Key");
        keyInfo.setDescription("Description Key To Update - test");
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"search"});
        keyInfo.setExpiresAt(dateParsed);

        KeyUpdate keyChanges = new KeyUpdate();
        keyInfo.setName("Key After Update");
        keyInfo.setDescription("Description Key After Update - test");

        Key createKey = client.createKey(keyInfo);
        Key updateKey = client.updateKey(createKey.getKey(), keyChanges);

        assertThat(createKey, is(instanceOf(Key.class)));
        assertThat(updateKey, is(instanceOf(Key.class)));
        assertThat(updateKey.getKey(), is(notNullValue()));
        assertThat(createKey.getIndexes()[0], is(equalTo("*")));
        assertThat(createKey.getActions()[0], is(equalTo("search")));
        assertThat(createKey.getName(), is(equalTo("Key After Update")));
        assertThat(updateKey.getDescription(), is(equalTo("Description Key After Update - test")));
        assertThat(updateKey.getIndexes()[0], is(equalTo(createKey.getIndexes()[0])));
        assertThat(updateKey.getActions()[0], is(equalTo(createKey.getActions()[0])));
        assertThat(updateKey.getExpiresAt(), is(equalTo(dateParsed)));
        assertThat(updateKey.getCreatedAt(), is(notNullValue()));
        assertThat(updateKey.getUpdatedAt(), is(notNullValue()));
    }

    /** Test Update an API Key with Jackson Json Handler */
    @Test
    public void testClientUpdateKeyWithJacksonJsonHandler() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateParsed = format.parse("2042-01-30T00:00:00Z");

        Key keyInfo = new Key();
        keyInfo.setName("Key");
        keyInfo.setDescription("Description Key To Update - test");
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"search"});
        keyInfo.setExpiresAt(dateParsed);

        KeyUpdate keyChanges = new KeyUpdate();
        keyInfo.setName("Key After Update");
        keyInfo.setDescription("Description Key After Update - test");

        Key createKey = clientJackson.createKey(keyInfo);
        Key updateKey = clientJackson.updateKey(createKey.getKey(), keyChanges);

        assertThat(createKey, is(instanceOf(Key.class)));
        assertThat(updateKey, is(instanceOf(Key.class)));
        assertThat(updateKey.getKey(), is(notNullValue()));
        assertThat(createKey.getIndexes()[0], is(equalTo("*")));
        assertThat(createKey.getActions()[0], is(equalTo("search")));
        assertThat(createKey.getName(), is(equalTo("Key After Update")));
        assertThat(updateKey.getDescription(), is(equalTo("Description Key After Update - test")));
        assertThat(updateKey.getIndexes()[0], is(equalTo(createKey.getIndexes()[0])));
        assertThat(updateKey.getActions()[0], is(equalTo(createKey.getActions()[0])));
        assertThat(updateKey.getExpiresAt(), is(equalTo(dateParsed)));
        assertThat(updateKey.getCreatedAt(), is(notNullValue()));
        assertThat(updateKey.getUpdatedAt(), is(notNullValue()));
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

    /** Test Delete an API Key With Uid */
    @Test
    public void testClientDeleteKeyWithUid() throws Exception {
        Key keyInfo = new Key();
        keyInfo.setIndexes(new String[] {"*"});
        keyInfo.setActions(new String[] {"*"});
        keyInfo.setExpiresAt(null);

        Key createKey = client.createKey(keyInfo);
        client.deleteKey(createKey.getUid());

        assertThrows(Exception.class, () -> client.getKey(createKey.getUid()));
    }
}
