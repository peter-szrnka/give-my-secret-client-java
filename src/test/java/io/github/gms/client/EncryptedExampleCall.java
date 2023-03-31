package io.github.gms.client;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class EncryptedExampleCall {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .withUrl("http://localhost:8080")
                .build();

        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        Map<String, String> response = client.getSecret(GetSecretRequest.builder()
                        .withKeystore(new FileInputStream(new File("src/test/resources/test.jks")))
                        .withKeystoreType(KeystoreType.JKS)
                        .withKeystoreCredential("test")
                        .withKeystoreAlias("test")
                        .withApiKey("<api_key>")
                        .withSecretId("<secret_id>")
                        .withSecretId("s2")
                .build());

        System.out.println("response = " + response.toString());
    }
}
