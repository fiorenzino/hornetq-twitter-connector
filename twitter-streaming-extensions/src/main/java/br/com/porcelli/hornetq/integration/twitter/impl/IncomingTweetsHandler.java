package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.Binding;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.utils.ConfigurationHelper;

import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class IncomingTweetsHandler implements ConnectorService {

	private final String connectorName;

	private final String queueName;

	private final Configuration conf;

	private final StorageManager storageManager;

	private final PostOffice postOffice;

	private TwitterStream twitterStream;

	private boolean isStarted = false;

	public IncomingTweetsHandler(final String connectorName,
			final Map<String, Object> configuration,
			final StorageManager storageManager, final PostOffice postOffice) {
		this.connectorName = connectorName;

		this.conf = new ConfigurationBuilder()
				.setOAuthConsumerKey(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.CONSUMER_KEY, null,
								configuration))
				.setOAuthConsumerSecret(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.CONSUMER_SECRET, null,
								configuration))
				.setOAuthAccessToken(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.ACCESS_TOKEN, null,
								configuration))
				.setOAuthAccessTokenSecret(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.ACCESS_TOKEN_SECRET,
								null, configuration)).build();

		this.queueName = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.QUEUE_NAME, null, configuration);

		this.storageManager = storageManager;
		this.postOffice = postOffice;
	}

	public String getName() {
		return connectorName;
	}

	public void start() throws Exception {
		Binding b = postOffice.getBinding(new SimpleString(queueName));
		if (b == null) {
			throw new Exception(connectorName + ": queue " + queueName + " not found");
		}

		this.twitterStream = new TwitterStreamFactory(conf).getInstance();
		this.twitterStream.addListener(new TwitterUserStreamDefaultListener(this.postOffice, this.storageManager, this.queueName));

		this.twitterStream.user();

		isStarted = true;
	}

	public void stop() throws Exception {
		if (!isStarted) {
			return;
		}
		this.twitterStream.shutdown();
		isStarted = false;
	}

	public boolean isStarted() {
		return isStarted;
	}
}