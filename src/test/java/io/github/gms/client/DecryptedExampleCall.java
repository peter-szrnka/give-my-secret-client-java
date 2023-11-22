package io.github.gms.client;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;

import java.util.Map;

public class DecryptedExampleCall {

    public static void main(String[] args) throws Exception {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .url("https://localhost:8443")
                .build();

        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        Map<String, String> response = client.getSecret(GetSecretRequest.builder()
                        .apiKey("<api_key>")
                        .secretId("<secret_id>")
                .build());

        System.out.println("response = " + response.toString());
    }
}
