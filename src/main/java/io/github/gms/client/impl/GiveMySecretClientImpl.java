package io.github.gms.client.impl;

import io.github.gms.client.GiveMySecretClient;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import io.github.gms.client.util.ApiHttpClient;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.gms.client.util.Constants.SIMPLE_CREDENTIAL;
import static io.github.gms.client.util.Constants.TYPE;
import static io.github.gms.client.util.Constants.VALUE;

/**
 * Default implementation for client.
 *
 * @author Peter Szrnka
 * @since 1.0.0
 */
public class GiveMySecretClientImpl implements GiveMySecretClient {

    private final GiveMySecretClientConfig configuration;

    public GiveMySecretClientImpl(GiveMySecretClientConfig configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration is mandatory!");
        }

        validateConfiguration(configuration);
        this.configuration = configuration;
    }

    @Override
    public Map<String, String> getSecret(GetSecretRequest request) throws Exception {
        validateRequest(request);

        Map<String, String> httpResponse = ApiHttpClient.get(configuration, request);
        // Type will be presented only if the values are encrypted
        // In this case we have to return the response what we received
        String type = httpResponse.get(TYPE);

        if (type == null) {
            return httpResponse;
        }

        // Pre filter results before decryption
        httpResponse = httpResponse.entrySet().stream().filter(s -> !s.getKey().equals(TYPE))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() != null ? entry.getValue() : ""));

        return decryptWithKeystore(request, httpResponse, type);
    }

    private Map<String, String> decryptWithKeystore(GetSecretRequest request, Map<String, String> response, String type) throws Exception {
        validateKeystore(request);

        KeyStore ks;
        ks = loadKeystore(request);

        PrivateKey pk = (PrivateKey) ks.getKey(request.getKeystoreAlias(), request.getKeystoreAliasCredential().toCharArray());

        Cipher decrypt = Cipher.getInstance(pk.getAlgorithm());
        decrypt.init(Cipher.DECRYPT_MODE, pk);
        byte[] decryptedMessageBytes = decrypt.doFinal(Base64.getDecoder().decode(response.getOrDefault(VALUE, "")
                .getBytes(StandardCharsets.UTF_8)));

        return buildResponseMap(type, new String(decryptedMessageBytes));
    }

    private static Map<String, String> buildResponseMap(String type, String decryptedRawMessage) {
        if (type.equals(SIMPLE_CREDENTIAL)) {
            return Map.of(VALUE, decryptedRawMessage);
        }

        Map<String, String> responseMap = new HashMap<>();
        Stream.of(decryptedRawMessage.split(";")).forEach(item -> {
            String[] elements = item.split(":");
            responseMap.put(elements[0], elements[1]);
        });

        return responseMap;
    }

    private static KeyStore loadKeystore(GetSecretRequest request) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keystore = KeyStore.getInstance(request.getKeystoreType().getType());
        keystore.load(request.getKeystore(), request.getKeystoreCredential().toCharArray());

        return keystore;
    }

    private static void validateConfiguration(GiveMySecretClientConfig configuration) {
        if (configuration.getUrl() == null) {
            throw new IllegalArgumentException("Server URL is mandatory!");
        }
    }

    private static void validateRequest(GetSecretRequest request) {
        if (request.getApiKey() == null) {
            throw new IllegalArgumentException("API key is mandatory!");
        }

        if (request.getSecretId() == null) {
            throw new IllegalArgumentException("Secret Id is mandatory!");
        }
    }

    private static void validateKeystore(GetSecretRequest request) {
        if (request.getKeystore() == null || isKeystoreConfigNotValid(request)) {
            throw new IllegalArgumentException(
                    "Invalid configuration: All keystore parameter must be set if keystore stream defined!");
        }
    }

    private static boolean isKeystoreConfigNotValid(GetSecretRequest request) {
        return request.getKeystoreType() == null || request.getKeystoreCredential() == null || request.getKeystoreAlias() == null
                || request.getKeystoreAliasCredential() == null;
    }
}