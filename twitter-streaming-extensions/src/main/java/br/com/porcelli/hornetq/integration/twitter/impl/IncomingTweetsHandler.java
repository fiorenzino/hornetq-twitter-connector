package br.com.porcelli.hornetq.integration.twitter.impl;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.Binding;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractBaseStreamListener;

public class IncomingTweetsHandler implements ConnectorService {
	private static final Logger log = Logger
			.getLogger(IncomingTweetsHandler.class);

	private final String connectorName;

	private final String queueName;

	private final Configuration conf;

	private final StorageManager storageManager;

	private final PostOffice postOffice;

	private TwitterStream twitterStream;

	private final Set<Class<? extends AbstractBaseStreamListener>> listeners;

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
		String listners = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.STREAM_LISTENERS, null, configuration);
		if (listners == null || listners.trim().length() == 0) {
			this.listeners = null;
		} else {
			this.listeners = getListeners(listners);
		}

	}

	private Set<Class<? extends AbstractBaseStreamListener>> getListeners(
			String listners) {
		Set<Class<? extends AbstractBaseStreamListener>> result = new HashSet<Class<? extends AbstractBaseStreamListener>>();
		listners.replaceAll(",", ";");
		listners.replaceAll(":", ";");

		for (String activeListner : listners.split(";")) {
			try {
				Class<?> clazz = Class.forName(activeListner);
				if (AbstractBaseStreamListener.class.isAssignableFrom(clazz)) {
					result.add((Class<? extends AbstractBaseStreamListener>) clazz);
				}
			} catch (ClassNotFoundException e) {
				log.error("Twitter Listener '" + activeListner + "' not found");
			}
		}
		if (result.size() > 0) {
			return result;
		}
		return null;
	}

	public String getName() {
		return connectorName;
	}

	public void start() throws Exception {
		Binding b = postOffice.getBinding(new SimpleString(queueName));
		if (b == null) {
			throw new Exception(connectorName + ": queue " + queueName
					+ " not found");
		}
		if (this.listeners == null) {
			log.warn("There is no Listners, can't start the service.");
			return;
		}

		this.twitterStream = new TwitterStreamFactory(conf).getInstance();
		for (Class<? extends AbstractBaseStreamListener> activeListener : listeners) {
			AbstractBaseStreamListener newListener = buildListenerInstance(activeListener);
			if (newListener != null) {
				this.twitterStream.addListener(newListener);
			}
		}

		twitterStream.user();

		isStarted = true;
	}

	public <T extends AbstractBaseStreamListener> T buildListenerInstance(
			Class<T> clazz) {
		T listener = null;
		Class<?>[] constructorArgs = new Class[] { PostOffice.class,
				StorageManager.class, String.class };
		Object[] args = new Object[] { this.postOffice, this.storageManager,
				this.queueName };
		Constructor<T> constructor;

		try {
			constructor = clazz.getConstructor(constructorArgs);
			listener = (T) constructor.newInstance(args);
		} catch (Exception e) {
			log.error("Listener '");
		}
		return listener;
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