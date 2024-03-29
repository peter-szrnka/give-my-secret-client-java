package io.github.gms.client.impl;

import io.github.gms.client.enums.KeystoreType;

/**
 * @author Peter Szrnka
 * @since 1.0.0
 */
public class InputData {

	String apiKey;
	String secretId;
	boolean keystoreRequired;
	KeystoreType type;
	String keystoreCredential;
	String keystoreAlias;
	String keystoreAliasCredential;
	String expectedMessage;
	String value;

	private InputData(Builder builder) {
		this.apiKey = builder.apiKey;
		this.secretId = builder.secretId;
		this.keystoreRequired = builder.keystoreRequired;
		this.type = builder.type;
		this.keystoreCredential = builder.keystoreCredential;
		this.keystoreAlias = builder.keystoreAlias;
		this.keystoreAliasCredential = builder.keystoreAliasCredential;
		this.expectedMessage = builder.expectedMessage;
		this.value = builder.value;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String apiKey;
		private String secretId;
		private boolean keystoreRequired;
		private KeystoreType type;
		private String keystoreCredential;
		private String keystoreAlias;
		private String keystoreAliasCredential;
		private String expectedMessage;
		private String value;

		private Builder() {
		}

		public Builder withApiKey(String apiKey) {
			this.apiKey = apiKey;
			return this;
		}

		public Builder withSecretId(String secretId) {
			this.secretId = secretId;
			return this;
		}

		public Builder withKeystoreRequired(boolean keystoreRequired) {
			this.keystoreRequired = keystoreRequired;
			return this;
		}

		public Builder withType(KeystoreType type) {
			this.type = type;
			return this;
		}

		public Builder withKeystoreCredential(String keystoreCredential) {
			this.keystoreCredential = keystoreCredential;
			return this;
		}

		public Builder withKeystoreAlias(String keystoreAlias) {
			this.keystoreAlias = keystoreAlias;
			return this;
		}

		public Builder withKeystoreAliasCredential(String keystoreAliasCredential) {
			this.keystoreAliasCredential = keystoreAliasCredential;
			return this;
		}

		public Builder withExpectedMessage(String expectedMessage) {
			this.expectedMessage = expectedMessage;
			return this;
		}

		public Builder withValue(String value) {
			this.value = value;
			return this;
		}

		public InputData build() {
			return new InputData(this);
		}
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSecretId() {
		return secretId;
	}

	public boolean isKeystoreRequired() {
		return keystoreRequired;
	}

	public KeystoreType getType() {
		return type;
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

	public String getExpectedMessage() {
		return expectedMessage;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "InputData [apiKey=" + apiKey + ", secretId=" + secretId + ", keystoreRequired=" + keystoreRequired
				+ ", type=" + type + ", keystoreCredential=" + keystoreCredential + ", keystoreAlias=" + keystoreAlias
				+ ", keystoreAliasCredential=" + keystoreAliasCredential + ", expectedMessage=" + expectedMessage
				+ ", value=" + value + "]";
	}
}