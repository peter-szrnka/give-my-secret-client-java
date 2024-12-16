package io.github.gms.client;

import io.github.gms.client.model.GetSecretRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
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
	 * @throws NoSuchAlgorithmException If the algorithm is not supported
	 * @throws IOException If the connection is not available
	 * @throws KeyManagementException If the key management is not available
	 * @throws UnrecoverableKeyException If the key is not recoverable
	 * @throws NoSuchPaddingException If the padding is not supported
	 * @throws IllegalBlockSizeException If the block size is not supported
	 * @throws CertificateException If the certificate is not available
	 * @throws KeyStoreException If the keystore is not available
	 * @throws BadPaddingException If the padding is not supported
	 * @throws InvalidKeyException If the key is not valid
     */
	Map<String, String> getSecret(GetSecretRequest request) throws
			NoSuchAlgorithmException, IOException, KeyManagementException,
			UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, CertificateException,
			KeyStoreException, BadPaddingException, InvalidKeyException;
}