package io.github.gms.client.builder;

import io.github.gms.client.GiveMySecretClient;
import io.github.gms.client.impl.GiveMySecretClientImpl;
import io.github.gms.client.model.GiveMySecretClientConfig;

/**
 * Client object for Give My Secret.
 *
 * @author Peter Szrnka
 * @since 1.0.0
 */
public final class GiveMySecretClientBuilder {

    public static GiveMySecretClient create(GiveMySecretClientConfig config) {
        return new GiveMySecretClientImpl(config);
    }
}
