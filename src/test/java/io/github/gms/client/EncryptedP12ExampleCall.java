package io.github.gms.client;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class EncryptedP12ExampleCall {

    public static void main(String[] args) throws Exception {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .url("https://localhost:8443")
                .build();

        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        Map<String, String> response = client.getSecret(GetSecretRequest.builder()
                        .keystore(new FileInputStream(new File("src/test/resources/test.p12")))
                        .keystoreType(KeystoreType.PKCS12)
                        .keystoreCredential("test")
                        .keystoreAliasCredential("test")
                        .keystoreAlias("test")
                        .apiKey("<api_key>")
                        .secretId("<secret_id>")
                .build());

        System.out.println("response = " + response.toString());
    }
}
