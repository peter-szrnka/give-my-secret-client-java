package io.github.gms.client.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Connection manager class.
 *
 * @author Peter Szrnka
 * @since 0.1
 */
public class ApiHttpClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    private ApiHttpClient() {
    }

    public static Map<String, String> get(GiveMySecretClientConfig configuration, GetSecretRequest request) throws Exception {
        HttpClient httpClient = initHttpClient(configuration);
        HttpRequest httpRequest = initHttpRequest(configuration, request);

        HttpResponse<String> response;
        int retry = 0;

        Map<String, Exception> exceptions = new HashMap<>();

        while ((response = send(httpClient, httpRequest, exceptions)) == null) {
            retry++;

            if (retry == configuration.getMaxRetry()) {
                break;
            }
        }

        if (response == null) {
            throw new RuntimeException("Request failed after " + configuration.getMaxRetry() + " retries! Error(s): " +
                    String.join(",\r\n", exceptions.keySet()));
        }

        return OBJECT_MAPPER.readValue(response.body(), Map.class);
    }

    private static HttpResponse<String> send(HttpClient httpClient, HttpRequest httpRequest, Map<String, Exception> exceptions) {
        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            exceptions.put(e.getMessage(), e);
            return null;
        }
    }

    private static HttpClient initHttpClient(GiveMySecretClientConfig configuration) throws NoSuchAlgorithmException, KeyManagementException {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(configuration.getDefaultConnectionTimeout()));

        if (configuration.isDisableSslVerification()) {
            SSLParameters sslParameters = new SSLParameters();
            sslParameters.setEndpointIdentificationAlgorithm("");

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts(), new java.security.SecureRandom());

            Properties props = System.getProperties();
            props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

            httpClientBuilder.sslContext(sc).sslParameters(sslParameters);
        }

        return httpClientBuilder.build();
    }

    private static HttpRequest initHttpRequest(GiveMySecretClientConfig configuration, GetSecretRequest request) {
        return HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(configuration.getDefaultReadTimeout()))
                .GET()
                .header("x-api-key", request.getApiKey())
                .header("Accept-Charset", StandardCharsets.UTF_8.displayName())
                .header("Content-Type", "application/json")
                .uri(URI.create(configuration.getUrl() + "/api/secret/" + request.getSecretId()))
                .build();
    }

    private static TrustManager[] trustAllCerts() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Nothing to do here
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Nothing to do here
                    }
                }
        };
    }
}