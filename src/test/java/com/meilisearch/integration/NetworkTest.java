package com.meilisearch.integration;

import com.meilisearch.integration.classes.AbstractIT;
import com.meilisearch.sdk.model.Network;
import com.meilisearch.sdk.model.NetworksRemote;
import com.meilisearch.sdk.model.UpdateNetwork;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class NetworkTest extends AbstractIT {
    @BeforeEach
    void initialize() {
        this.setUp();
        this.setUpJacksonClient();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterEach
    void cleanMeilisearch() {
        cleanup();
    }

    @Test
    public void testGetNetworkState() {
        Network networkState = this.client.getNetwork();

        assertThat(networkState, is(notNullValue()));
        assertThat(networkState, is(instanceOf(Network.class)));
    }

    @Test
    public void patchNetworkState() {
        HashMap<String, NetworksRemote> remotes = new HashMap<>();
        remotes.put("ms-00", new NetworksRemote(
            "http://INSTANCE_URL",
            "INSTANCE_API_KEY",
            null));
        remotes.put("ms-01", new NetworksRemote(
            "http://ANOTHER_INSTANCE_URL",
            "ANOTHER_INSTANCE_API_KEY",
            null));
        UpdateNetwork newNetwork = new UpdateNetwork();
        newNetwork.setSelf("ms-00");
        newNetwork.setRemotes(remotes);

        var network = this.client.updateNetwork(newNetwork);

        assertThat(network, is(notNullValue()));
        assertThat(network.getSelf(), is("ms-00"));
        assertThat(network.getRemotes(), aMapWithSize(2));
    }
}
