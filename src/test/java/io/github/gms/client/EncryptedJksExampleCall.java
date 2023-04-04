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

public class EncryptedJksExampleCall {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .withUrl("https://localhost:8443")
                .build();

        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        Map<String, String> response = client.getSecret(GetSecretRequest.builder()
                        .withKeystore(new FileInputStream(new File("src/test/resources/test.jks")))
                        .withKeystoreType(KeystoreType.JKS)
                        .withKeystoreCredential("test")
                        .withKeystoreAliasCredential("test")
                        .withKeystoreAlias("test")
                        .withApiKey("<api_key>")
                        .withSecretId("<secret_id>")
                .build());

        System.out.println("response = " + response.toString());
    }
}
