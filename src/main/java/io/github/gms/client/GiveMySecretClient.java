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
	 * @param request
	 * @return GetSecretResponse Response object
	 * 
	 * @throws Exception
	 */
	Map<String, String> getSecret(GetSecretRequest request) throws Exception;
}