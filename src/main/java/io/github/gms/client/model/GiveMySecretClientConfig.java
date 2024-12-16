package io.github.gms.client.model;

/**
 * Client configuration
 * 
 * @author Peter Szrnka
 * @since 1.0
 */
public class GiveMySecretClientConfig {

	public static final int DEFAULT_MAX_RETRY = 3;
	public static final int DEFAULT_RETRY_DELAY = 1000;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
	public static final int DEFAULT_READ_TIMEOUT = 30000;
	public static final boolean DEFAULT_DISABLE_SSL_VERIFICATION = false;

	private final String url;
	private final int defaultConnectionTimeout;
	private final int defaultReadTimeout;
	private final boolean disableSslVerification;
	private final int maxRetry;
	private final int retryDelay;

	private GiveMySecretClientConfig(Builder builder) {
		this.url = builder.url;
		this.defaultConnectionTimeout = builder.defaultConnectionTimeout;
		this.defaultReadTimeout = builder.defaultReadTimeout;
		this.disableSslVerification = builder.disableSslVerification;
		this.maxRetry = builder.maxRetry;
		this.retryDelay = builder.retryDelay;
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

	public int getRetryDelay() {
		return retryDelay;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private String url;
		private int defaultConnectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
		private int defaultReadTimeout = DEFAULT_READ_TIMEOUT;
		private boolean disableSslVerification = DEFAULT_DISABLE_SSL_VERIFICATION;
		private int maxRetry = DEFAULT_MAX_RETRY;
		private int retryDelay = DEFAULT_RETRY_DELAY;

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

		public Builder retryDelay(int retryDelay) {
			this.retryDelay = retryDelay;
			return this;
		}
	}
}