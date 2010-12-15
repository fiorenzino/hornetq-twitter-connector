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

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractBaseStreamListener;

public abstract class BaseStreamHandler<T extends AbstractBaseStreamListener>
		implements ConnectorService {
	private static final Logger log = Logger.getLogger(BaseStreamHandler.class);

	protected final String connectorName;

	protected final String queueName;

	protected final Configuration conf;

	protected final StorageManager storageManager;

	protected final PostOffice postOffice;

	protected TwitterStream twitterStream;

	protected final Set<Class<T>> listeners;

	protected boolean isStarted = false;

	public BaseStreamHandler(final String connectorName,
			final Map<String, Object> configuration,
			final StorageManager storageManager, final PostOffice postOffice) {
		this.connectorName = connectorName;

		this.conf = new ConfigurationBuilder()
				.setOAuthConsumerKey(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.PROP_CONSUMER_KEY,
								null, configuration))
				.setOAuthConsumerSecret(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.PROP_CONSUMER_SECRET,
								null, configuration))
				.setOAuthAccessToken(
						ConfigurationHelper.getStringProperty(
								InternalTwitterConstants.PROP_ACCESS_TOKEN,
								null, configuration))
				.setOAuthAccessTokenSecret(
						ConfigurationHelper
								.getStringProperty(
										InternalTwitterConstants.PROP_ACCESS_TOKEN_SECRET,
										null, configuration)).build();

		this.queueName = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.PROP_QUEUE_NAME, null, configuration);

		this.storageManager = storageManager;
		this.postOffice = postOffice;
		String listners = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.PROP_STREAM_LISTENERS, null,
				configuration);
		if (listners == null || listners.trim().length() == 0) {
			this.listeners = null;
		} else {
			this.listeners = getListeners(listners);
		}
	}

	protected Set<Class<T>> getListeners(final String listners) {
		Set<Class<T>> result = new HashSet<Class<T>>();

		for (String activeListner : splitProperty(listners)) {
			try {
				Class<?> clazz = Class.forName(activeListner);
				if (AbstractBaseStreamListener.class.isAssignableFrom(clazz)) {
					result.add((Class<T>) clazz);
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

	protected String[] splitProperty(final String propertyValue) {
		if (propertyValue == null || propertyValue.trim().length() == 0) {
			return null;
		}

		return propertyValue.replace(',', ';').replace(':', ';').split(";");
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

		startStreaming();

		isStarted = true;
	}

	protected abstract void startStreaming() throws TwitterException;

	public T buildListenerInstance(Class<T> clazz) {
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