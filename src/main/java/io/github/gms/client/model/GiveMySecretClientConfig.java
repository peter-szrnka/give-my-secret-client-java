package io.github.gms.client.model;

/**
 * Client configuration
 * 
 * @author Peter Szrnka
 * @since 1.0
 */
public class GiveMySecretClientConfig {

	private final String url;
	private final int defaultConnectionTimeout;
	private final int defaultReadTimeout;
	private final boolean disableSslVerification;
	private final int maxRetry;

	private GiveMySecretClientConfig(Builder builder) {
		this.url = builder.url;
		this.defaultConnectionTimeout = builder.defaultConnectionTimeout;
		this.defaultReadTimeout = builder.defaultReadTimeout;
		this.disableSslVerification = builder.disableSslVerification;
		this.maxRetry = builder.maxRetry;
	}

	public String getUrl() {
		return url;
	}

	public int getDefaultConnectionTimeout() {
		return defaultConnectionTimeout;
	}

	public int getDefaultReadTimeout() {
		return defaultReadTimeout;
	}

	public boolean isDisableSslVerification() {
		return disableSslVerification;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String url;
		private int defaultConnectionTimeout = 30000;
		private int defaultReadTimeout = 30000;
		private boolean disableSslVerification = true;
		private int maxRetry = 3;

		private Builder() {
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder disableSslVerification(boolean disableSslVerification) {
			this.disableSslVerification = disableSslVerification;
			return this;
		}

		public Builder defaultConnectionTimeout(int defaultConnectionTimeout) {
			this.defaultConnectionTimeout = defaultConnectionTimeout;
			return this;
		}

		public Builder defaultReadTimeout(int defaultReadTimeout) {
			this.defaultReadTimeout = defaultReadTimeout;
			return this;
		}

		public GiveMySecretClientConfig build() {
			return new GiveMySecretClientConfig(this);
		}

		public Builder maxRetry(int maxRetry) {
			this.maxRetry = maxRetry;
			return this;
		}
	}
}