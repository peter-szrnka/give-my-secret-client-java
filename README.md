# give-my-secret-client-java
Official Java client library for [**Give My Secret**](https://github.com/peter-szrnka/give-my-secret) credential storage application. 

# Getting started

## Maven dependency

To use the official Java client you have to add the library artifact to your Maven pom.xml:

```
<parent>
        <groupId>io.github.peter-szrnka</groupId>
        <artifactId>give-my-secret-client-java</artifactId>
        <version>1.0.0</version>
    </parent>
```

## Code sample

You will need a couple of items to run the code:

- A valid PKCS12 / JKS keystore with at least one entry
- Keystore credential
- Keystore alias
- Keystore alias credential
- API key
- Secret ID

```
public class MavenClientLibraryExample {

	public static void main(String[] args) throws Exception {
		GiveMySecretClient client = GiveMySecretClientBuilder.create(GiveMySecretClientConfig.builder()
				.url(getInstanceUrl())
				.build());

		Map<String, String> response = client.getSecret(GetSecretRequest.builder()
						.keystore(new FileInputStream("src/main/resources/test.p12"))
						.keystoreCredential("test")
						.keystoreAlias("test")
						.keystoreAliasCredential("test")
						.keystoreType(KeystoreType.PKCS12)
				.apiKey(System.getenv("API_KEY"))
				.secretId(System.getenv("SECRET_ID"))
				.build());

		System.out.println("Response = " + response.get("value"));
	}

	private static String getInstanceUrl() {
		return Optional.ofNullable(System.getenv("GMS_URL")).orElse("https://localhost:8443");
	}
}
```



# Code quality & health


| Code QL                                                      | Code coverage                                                | Sonarcloud                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| [![CodeQL](https://github.com/peter-szrnka/give-my-secret-client-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/peter-szrnka/give-my-secret-client-java/actions/workflows/codeql.yml) | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=peter-szrnka_give-my-secret-client-java&metric=coverage)](https://sonarcloud.io/summary/new_code?id=szrnka-peter_give-my-secret-client-java) | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=peter-szrnka_give-my-secret-client-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=peter-szrnka_give-my-secret-client-java) |
