package io.github.gms.client.impl;

import io.github.gms.client.GiveMySecretClient;
import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import io.github.gms.client.util.ApiHttpClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.github.gms.client.util.Constants.MULTIPLE_CREDENTIAL;
import static io.github.gms.client.util.Constants.SIMPLE_CREDENTIAL;
import static io.github.gms.client.util.Constants.TYPE;
import static io.github.gms.client.util.Constants.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit test of {@link GiveMySecretClientImpl}
 * @author Peter Szrnka
 */
@ExtendWith(MockitoExtension.class)
class GiveMySecretClientImplTest {

	private static final String TEST_DECRYPTED_VALUE = "my-value";
	private static final String TEST_P12_VALUE = "FI2jIqPs5aD/Bf0V1Kz093kGu07nZ/czvILHmxkE88jhHITItId/hMiRO+ZQ4WcSQtf3IQlbROr86tjbPeTXU7QGz55OvCcjGSPh4EfKfazrbk1MhKAvZM21/KQ+zaiB0wJxGYpWd5In2GTaLLfJPF49+yfe+ncTC/VwuXSuXEdTTuWV/xRJkHyjIZqrJqqXMu/u64zIwTRzNzsDpFTFcVYimLHfFON1CN9IbyzEgV+8+Cu0S9dEIH3ZQowh63kThVCzPGNWeJorpQ7b9jHQFBDc0j4E+3Bdc9JocYOg8I8d0urYBJIZ3FaZC3woAjhdC0GwdhkC0rn2xaWXRv/BCQ";
	private static final String TEST_ENCRYPTED_VALUE = "fTFllqRl39VoaYwCdpDNP1CAHWdhFCLUSJf+OOJyRzc05x1PslQihY4NM67LTAocOga1iePFNps0F4VL//kuXQV+rpLPowu7rvoVZl90Gau3fnF2ck7C2CY1ScBW0nIFuuEe+eya1eAFbMYQrYFx2NyaWug6ARfJxOxgcNYAW3av3rMkKw2CsjgAg7OHrg2f6d4TzxYoUVrcmMu+dziKf+vWAkKRuS+rg4NKmCkFg8hj1haa7Or8SNr+iBgx2TBAOEpPhidS6W/Mu5kmS9q5tI8+TPF2MjnHsGGRvKzQZilFSwk3C34BmiDiqtvYeDTfYaQsavGXd9ggarR2sQhkMA";
	private static final String TEST_MULTIPLE_ENCRYPTED_VALUE = "iVLBe8/VeA+Bw3jyxtqo0fRw3lSLYky/q+DiAQjuzqmGJxNDKY7c+/+Z++stxibDinAMRNAFDgG+XKOSFfgL6Vykja513hskoI18Ome9CVCCJjsFN4tjfEu7aomnShsQo+rF/QFiDPwd0Hy+JmvtLsCYhpANjFIIxYWYcKS1TG47SjgjL9jkY7u1TGfPIh/Zgjv5UhE1qHMVj8SK/ILYyxRJp/YrxXrUhyR0q5kbgzLwwyMErG68so0NZGAuAYEido/EP/JMExjdxkYdYPWVGb9YezDJ6fl9Z8fgm786vynDV/dvG0T/dSh1c6epS1y43C7izh647Yysc0AzMX6pBQ";
	private InputStream jksKeystoreStream;
	private InputStream p12KeystoreStream;

	@BeforeEach
	public void setup() {
		jksKeystoreStream = getClass().getClassLoader().getResourceAsStream("test.jks");
		p12KeystoreStream = getClass().getClassLoader().getResourceAsStream("test.p12");
	}

	@Test
	void shouldFailWhenServerUrlIsMissing() {
		// act
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder().build();
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> GiveMySecretClientBuilder.create(config));

		// arrange
		assertEquals("Server URL is mandatory!", exception.getMessage());
	}

	@Test
	void shouldFailWhenNoConfigProvided() {
		// act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> GiveMySecretClientBuilder.create(null));

		// arrange
		assertEquals("Configuration is mandatory!", exception.getMessage());
	}

	@Test
	@SneakyThrows
	void shouldDecryptMultipleCredential() {
		// arrange
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
				.url("server:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.apiKey("api-key")
				.keystoreType(KeystoreType.JKS)
				.keystore(jksKeystoreStream)
				.secretId("secret")
				.keystoreAliasCredential("test")
				.keystoreCredential("test")
				.keystoreAlias("test")
				.build();

		try (MockedStatic<ApiHttpClient> mockedConnectionUtils = mockStatic(ApiHttpClient.class)) {
			Map<String, String> mockMap = new HashMap<>();
			mockMap.put(VALUE, TEST_MULTIPLE_ENCRYPTED_VALUE);
			mockMap.put(TYPE, MULTIPLE_CREDENTIAL);
			mockedConnectionUtils.when(() -> ApiHttpClient.get(config, request)).thenReturn(mockMap);

			// act
			GiveMySecretClient client = GiveMySecretClientBuilder.create(config);
			Map<String, String> response = client.getSecret(request);

			// assert
			assertNotNull(response);
			assertEquals("u", response.get("username"));
			assertEquals("p", response.get("password"));
		}
	}

	@ParameterizedTest
	@MethodSource("inputData")
	void shouldTestWithJks(InputData input) {
		// arrange
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
				.url("server:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.apiKey(input.getApiKey())
				.keystoreType(input.getType())
				.keystore(input.isKeystoreRequired() ? jksKeystoreStream : null)
				.secretId(input.getSecretId())
				.keystoreAliasCredential(input.getKeystoreAliasCredential())
				.keystoreCredential(input.getKeystoreCredential())
				.keystoreAlias(input.getKeystoreAlias())
				.build();

		try (MockedStatic<ApiHttpClient> mockedConnectionUtils = mockStatic(ApiHttpClient.class)) {
			Map<String, String> mockMap = new HashMap<>();
			mockMap.put(VALUE, input.getValue());
			mockMap.put(TYPE, SIMPLE_CREDENTIAL);
			mockedConnectionUtils.when(() -> ApiHttpClient.get(config, request)).thenReturn(mockMap);

			// act
			GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

			if (input.getExpectedMessage() != null) {
				Exception exception = assertThrows(Exception.class, () -> client.getSecret(request));

				// arrange
				assertNotNull(exception);
				assertEquals(input.getExpectedMessage(), exception.getMessage());
			} else {
				try {
					Map<String, String> response = client.getSecret(request);
					assertNotNull(response);
					assertEquals("my-value", response.get(VALUE));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SneakyThrows
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void shouldTestWithP12(boolean returnDecrypted) {
		// arrange
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
				.url("http://localhost:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.apiKey("apiKey")
				.keystoreType(KeystoreType.PKCS12)
				.keystore(p12KeystoreStream)
				.secretId("secret2")
				.keystoreAliasCredential("test")
				.keystoreCredential("test")
				.keystoreAlias("test")
				.build();

		Map<String, String> mockMap = new HashMap<>();
		if (returnDecrypted) {
			mockMap.put("username", "peter");
			mockMap.put("password", "asdf1234");
		} else {
			mockMap.put(VALUE, TEST_P12_VALUE);
			mockMap.put(TYPE, MULTIPLE_CREDENTIAL);
		}

		try (MockedStatic<ApiHttpClient> mockedConnectionUtils = mockStatic(ApiHttpClient.class)) {
			mockedConnectionUtils.when(() -> ApiHttpClient.get(config, request)).thenReturn(mockMap);

			// act
			GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

			Map<String, String> response = client.getSecret(request);
			assertNotNull(response);
			assertEquals("peter", response.get("username"));
			assertEquals("asdf1234", response.get("password"));
		}
	}

	public static InputData[] inputData() {
		return new InputData[] {
				// API key is missing
				InputData.builder().withExpectedMessage("API key is mandatory!").build(),
				// SecretId is missing
				InputData.builder().withApiKey("apiKey").withExpectedMessage("Secret Id is mandatory!").build(),
				// Successful execution with decrypted data
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withValue(TEST_DECRYPTED_VALUE).build(),
				// Keystore credential missing
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true)
						.withExpectedMessage("Invalid configuration: All keystore parameter must be set if keystore stream defined!").build(),
				// Keystore credential missing
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS)
						.withExpectedMessage("Invalid configuration: All keystore parameter must be set if keystore stream defined!").build(),
				// Keystore alias missing
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS).withKeystoreCredential("test")
						.withExpectedMessage("Invalid configuration: All keystore parameter must be set if keystore stream defined!").build(),
				// Keystore alias credential missing
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS)
						.withKeystoreCredential("test").withKeystoreAlias("test")
						.withExpectedMessage("Invalid configuration: All keystore parameter must be set if keystore stream defined!").build(),
				// Failed execution with encrypted data
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS)
						
						.withKeystoreAlias("test").withKeystoreCredential("test").withKeystoreAliasCredential("test").withValue("invalid").withExpectedMessage("Decryption error").build(),
				// Successful execution with encrypted data
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS)
						
						.withKeystoreAlias("test").withKeystoreCredential("test").withKeystoreAliasCredential("test").withValue(TEST_ENCRYPTED_VALUE).build(),
		};
	}
}
