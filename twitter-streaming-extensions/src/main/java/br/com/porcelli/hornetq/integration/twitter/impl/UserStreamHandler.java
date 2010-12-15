package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.TwitterStreamFactory;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractUserBaseStreamListener;

public class UserStreamHandler extends
		BaseStreamHandler<AbstractUserBaseStreamListener> {

	public UserStreamHandler(final String connectorName,
			final Map<String, Object> configuration,
			final StorageManager storageManager, final PostOffice postOffice) {
		super(connectorName, configuration, storageManager, postOffice);
	}

	protected void startStreaming() {
		this.twitterStream = new TwitterStreamFactory(conf).getInstance();
		for (Class<? extends AbstractUserBaseStreamListener> activeListener : listeners) {
			AbstractUserBaseStreamListener newListener = buildListenerInstance((Class<AbstractUserBaseStreamListener>) activeListener);
			if (newListener != null) {
				this.twitterStream.addListener(newListener);
			}
		}

		twitterStream.user();
	}

}