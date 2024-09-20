package io.github.gms.client;

import io.github.gms.client.model.GetSecretRequest;

import java.util.Map;

/**
 * Client object for Give My Secret.
 * 
 * @author Peter Szrnka
 * @since 1.0.0
 */
public interface GiveMySecretClient {

	/**
	 * Returns with a secret by the provided input
	 * @param request A prepared {@link GetSecretRequest} object
	 * @return {@link Map} Response object
	 *
	 * @throws Exception If any error occurs an error will be thrown
     */
	Map<String, String> getSecret(GetSecretRequest request) throws Exception;
}