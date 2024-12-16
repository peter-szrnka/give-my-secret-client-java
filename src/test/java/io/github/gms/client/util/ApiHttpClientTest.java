package io.github.gms.client.util;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.github.gms.client.util.Constants.VALUE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of {@link ApiHttpClient}
 *
 * @author Peter Szrnka
 * @since 1.0.0
 */
@WireMockTest(httpsPort = 9443, httpsEnabled = true)
class ApiHttpClientTest {

    @Test
    void shouldThrowRuntimeException() {
        // arrange
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/secret/secret1"))
                .willReturn(WireMock.aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                        .withHeader("Content-Type", "application/json")
                        .withStatus(500)
                        .withBody("{ \"value\" : \"my-value\"}")));

        // act
        GiveMySecretClientConfig configuration = GiveMySecretClientConfig.builder()
                .url("https://localhost:9443")
                .defaultConnectionTimeout(60000)
                .defaultReadTimeout(60000)
                .disableSslVerification(true)
                .maxRetry(2)
				.retryDelay(10)
                .build();
        GetSecretRequest request = GetSecretRequest.builder().apiKey("api-key").secretId("secret1").build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ApiHttpClient.get(configuration, request));

        assertNotNull(exception);
        assertEquals("Request failed after 2 retries! Error(s): Illegal character in chunk size: 108", exception.getMessage());
    }

    @Test
    void get_whenSslVerificationFailed_thenThrowException() {
        // arrange
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/secret/secret1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"value\" : \"my-value\"}")));

        // act
        GiveMySecretClientConfig configuration = GiveMySecretClientConfig.builder()
                .url("https://localhost:9443")
                .defaultConnectionTimeout(60000)
                .defaultReadTimeout(60000)
                .disableSslVerification(false)
                .maxRetry(2)
                .build();
        GetSecretRequest request = GetSecretRequest.builder().apiKey("api-key").secretId("secret1").build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ApiHttpClient.get(configuration, request));

        assertNotNull(exception);
        assertEquals("Request failed after 2 retries! Error(s): PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target", exception.getMessage());
    }

    @Test
    void get_whenHttpCallCompleted_thenReturnResponse() throws Exception {
        // arrange
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/secret/secret1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{ \"value\" : \"my-value\"}")));

        // act
        GiveMySecretClientConfig configuration = GiveMySecretClientConfig.builder()
                .url("https://localhost:9443")
                .defaultConnectionTimeout(60000)
                .defaultReadTimeout(60000)
                .disableSslVerification(true)
                .build();
        GetSecretRequest request = GetSecretRequest.builder().apiKey("api-key").secretId("secret1").build();

        Map<String, String> response = ApiHttpClient.get(configuration, request);
        assertNotNull(response);
        assertEquals("my-value", response.get(VALUE));
    }

    @Test
    void delay_whenSleepFunctionThrowsInterruptedException_thenThreadInterrupted() {
        // arrange
        ApiHttpClient.SleepFunction sleepFunction = () -> {
            throw new InterruptedException();
        };

        // act
        ApiHttpClient.delay(sleepFunction);

        // assert
        assertTrue(Thread.currentThread().isInterrupted());
    }
}