package io.github.gms.client.impl;

import io.github.gms.client.GiveMySecretClient;
import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import io.github.gms.client.util.HttpClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
	private static final String TEST_P12_VALUE = "cWp12SWr4rnBbScCBDrmgM+MwzpN9SDtYjJqouMJ7MQba3zElnfS8xm052upF8mcjM06h2Afm+M2FUEA3Un9sspGSMzhhSXTLSwhOe1amdzuLuIcxvFEA+b4fI2FUESlpcEnmcNghlinFQwwGIy7eEKhw0q7HxIjW5y733lUL/0wMd1FFv40NojDzmqoDMl5byjn0VV3aE1pKddSAzxs1xn4O2spRL2QyK+ILt83vFesSlPiRjZtMta4/ERGUPlyZPBKJlxJMNPwGEdca/mj0f4IhfdR+CI2Mu4CPqV6ev/IGsoBXMBurBsuSpWDKN/zuldpwlYONqz0jcXWgINYNA";
	private static final String TEST_ENCRYPTED_VALUE = "fTFllqRl39VoaYwCdpDNP1CAHWdhFCLUSJf+OOJyRzc05x1PslQihY4NM67LTAocOga1iePFNps0F4VL//kuXQV+rpLPowu7rvoVZl90Gau3fnF2ck7C2CY1ScBW0nIFuuEe+eya1eAFbMYQrYFx2NyaWug6ARfJxOxgcNYAW3av3rMkKw2CsjgAg7OHrg2f6d4TzxYoUVrcmMu+dziKf+vWAkKRuS+rg4NKmCkFg8hj1haa7Or8SNr+iBgx2TBAOEpPhidS6W/Mu5kmS9q5tI8+TPF2MjnHsGGRvKzQZilFSwk3C34BmiDiqtvYeDTfYaQsavGXd9ggarR2sQhkMA";
	private static final String TEST_MULTIPLE_ENCRYPTED_VALUE = "iVLBe8/VeA+Bw3jyxtqo0fRw3lSLYky/q+DiAQjuzqmGJxNDKY7c+/+Z++stxibDinAMRNAFDgG+XKOSFfgL6Vykja513hskoI18Ome9CVCCJjsFN4tjfEu7aomnShsQo+rF/QFiDPwd0Hy+JmvtLsCYhpANjFIIxYWYcKS1TG47SjgjL9jkY7u1TGfPIh/Zgjv5UhE1qHMVj8SK/ILYyxRJp/YrxXrUhyR0q5kbgzLwwyMErG68so0NZGAuAYEido/EP/JMExjdxkYdYPWVGb9YezDJ6fl9Z8fgm786vynDV/dvG0T/dSh1c6epS1y43C7izh647Yysc0AzMX6pBQ";
	private InputStream jksKeystoreStream;
	private InputStream p12KeystoreStream;

	@BeforeEach
	private void setup() {
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
				.withUrl("server:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.withApiKey("api-key")
				.withKeystoreType(KeystoreType.JKS)
				.withKeystore(jksKeystoreStream)
				.withSecretId("secret")
				.withKeystoreAliasCredential("test")
				.withKeystoreCredential("test")
				.withKeystoreAlias("test")
				.build();

		MockedStatic<HttpClient> mockedConnectionUtils = mockStatic(HttpClient.class);
		Map<String, String> mockMap = new HashMap<>();
		mockMap.put(VALUE, TEST_MULTIPLE_ENCRYPTED_VALUE);
		mockMap.put(TYPE, MULTIPLE_CREDENTIAL);
		mockedConnectionUtils.when(() -> HttpClient.getResponse(config, request)).thenReturn(mockMap);

		// act
		GiveMySecretClient client = GiveMySecretClientBuilder.create(config);
		Map<String, String> response = client.getSecret(request);

		// assert
		assertNotNull(response);
		assertEquals("u", response.get("username"));
		assertEquals("p", response.get("password"));

		mockedConnectionUtils.close();
	}

	@ParameterizedTest
	@MethodSource("inputData")
	void shouldTestWithJks(InputData input) {
		// arrange
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
				.withUrl("server:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.withApiKey(input.getApiKey())
				.withKeystoreType(input.getType())
				.withKeystore(input.isKeystoreRequired() ? jksKeystoreStream : null)
				.withSecretId(input.getSecretId())
				.withKeystoreAliasCredential(input.getKeystoreAliasCredential())
				.withKeystoreCredential(input.getKeystoreCredential())
				.withKeystoreAlias(input.getKeystoreAlias())
				.build();

		MockedStatic<HttpClient> mockedConnectionUtils = mockStatic(HttpClient.class);
		Map<String, String> mockMap = new HashMap<>();
		mockMap.put(VALUE, input.getValue());
		mockMap.put(TYPE, SIMPLE_CREDENTIAL);
		mockedConnectionUtils.when(() -> HttpClient.getResponse(config, request)).thenReturn(mockMap);

		// act
		GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

		if (input.getExpectedMessage() != null) {
			Exception exception = assertThrows(Exception.class, () -> client.getSecret(request));

			// arrange
			assertEquals(input.getExpectedMessage(), exception.getMessage());

			mockedConnectionUtils.close();
		} else {
			try {
				Map<String, String> response = client.getSecret(request);
				assertNotNull(response);
				assertEquals("my-value", response.get(VALUE));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mockedConnectionUtils.close();
			}
		}
	}

	@Test
	@SneakyThrows
	void shouldTestWithP12() {
		// arrange
		GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
				.withUrl("http://localhost:8080")
				.build();

		GetSecretRequest request = GetSecretRequest.builder()
				.withApiKey("apiKey")
				.withKeystoreType(KeystoreType.PKCS12)
				.withKeystore(p12KeystoreStream)
				.withSecretId("secret1")
				.withKeystoreAliasCredential("Test1234")
				.withKeystoreCredential("Test1234")
				.withKeystoreAlias("test")
				.build();

		MockedStatic<HttpClient> mockedConnectionUtils = mockStatic(HttpClient.class);
		mockedConnectionUtils.when(() -> HttpClient.getResponse(config, request)).thenReturn(Map.of(VALUE, TEST_P12_VALUE));

		// act
		GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

		Map<String, String> response = client.getSecret(request);
		assertNotNull(response);
		assertEquals("my-value", response.get(VALUE));

		mockedConnectionUtils.close();
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
						
						.withKeystoreAlias("test").withKeystoreCredential("test").withKeystoreAliasCredential("test").withValue("invalid").withExpectedMessage("Message cannot be decrypted!").build(),
				// Successful execution with encrypted data
				InputData.builder().withApiKey("apiKey").withSecretId("secretId").withKeystoreRequired(true).withType(KeystoreType.JKS)
						
						.withKeystoreAlias("test").withKeystoreCredential("test").withKeystoreAliasCredential("test").withValue(TEST_ENCRYPTED_VALUE).build(),
		};
	}
}
