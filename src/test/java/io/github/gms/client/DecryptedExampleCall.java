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

public class DecryptedExampleCall {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .withUrl("http://localhost:8080")
                .build();

        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        Map<String, String> response = client.getSecret(GetSecretRequest.builder()
                        .withApiKey("<api_key>")
                        .withSecretId("<secret_id>")
                .build());

        System.out.println("response = " + response.toString());
    }
}
