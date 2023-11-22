package io.github.gms.client.model;

import io.github.gms.client.enums.KeystoreType;

import java.io.InputStream;

/**
 * @author Peter Szrnka
 * @since 1.0.0
 */
public class GetSecretRequest {

	private final String apiKey;
	private final String secretId;
	private final InputStream keystore;
	private final KeystoreType keystoreType;
	private final String keystoreCredential;
	private final String keystoreAlias;
	private final String keystoreAliasCredential;

	private GetSecretRequest(Builder builder) {
		this.apiKey = builder.apiKey;
		this.secretId = builder.secretId;
		this.keystore = builder.keystore;
		this.keystoreType = builder.keystoreType;
		this.keystoreCredential = builder.keystoreCredential;
		this.keystoreAlias = builder.keystoreAlias;
		this.keystoreAliasCredential = builder.keystoreAliasCredential;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String apiKey;
		private String secretId;
		private InputStream keystore;
		private KeystoreType keystoreType;
		private String keystoreCredential;
		private String keystoreAlias;
		private String keystoreAliasCredential;

		private Builder() {
		}

		public Builder apiKey(String apiKey) {
			this.apiKey = apiKey;
			return this;
		}

		public Builder secretId(String secretId) {
			this.secretId = secretId;
			return this;
		}

		public Builder keystore(InputStream keystore) {
			this.keystore = keystore;
			return this;
		}

		public Builder keystoreType(KeystoreType keystoreType) {
			this.keystoreType = keystoreType;
			return this;
		}

		public Builder keystoreCredential(String keystoreCredential) {
			this.keystoreCredential = keystoreCredential;
			return this;
		}

		public Builder keystoreAlias(String keystoreAlias) {
			this.keystoreAlias = keystoreAlias;
			return this;
		}

		public Builder keystoreAliasCredential(String keystoreAliasCredential) {
			this.keystoreAliasCredential = keystoreAliasCredential;
			return this;
		}

		public GetSecretRequest build() {
			return new GetSecretRequest(this);
		}
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSecretId() {
		return secretId;
	}

	public InputStream getKeystore() {
		return keystore;
	}

	public KeystoreType getKeystoreType() {
		return keystoreType;
	}

	public String getKeystoreCredential() {
		return keystoreCredential;
	}

	public String getKeystoreAlias() {
		return keystoreAlias;
	}

	public String getKeystoreAliasCredential() {
		return keystoreAliasCredential;
	}
}