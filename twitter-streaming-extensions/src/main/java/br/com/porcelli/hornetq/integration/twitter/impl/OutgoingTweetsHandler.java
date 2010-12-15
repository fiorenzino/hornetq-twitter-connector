/*
 * Copyright 2009 Red Hat, Inc.
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.filter.Filter;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.Binding;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.core.server.Consumer;
import org.hornetq.core.server.HandleStatus;
import org.hornetq.core.server.MessageReference;
import org.hornetq.core.server.Queue;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.GeoLocation;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;

/**
 * OutgoingTweetsHandler consumes from configured HornetQ address and forwards
 * to the twitter.
 */
public class OutgoingTweetsHandler implements Consumer, ConnectorService {
	private static final Logger log = Logger
			.getLogger(OutgoingTweetsHandler.class);

	private final String connectorName;

	private final String consumerKey;

	private final String consumerSecret;

	private final String accessToken;

	private final String accessTokenSecret;

	private final String queueName;

	private final PostOffice postOffice;

	private Twitter twitter = null;

	private Queue queue = null;

	private Filter filter = null;

	private boolean isStarted = false;

	public OutgoingTweetsHandler(final String connectorName,
			final Map<String, Object> configuration, final PostOffice postOffice) {
		this.connectorName = connectorName;
		this.consumerKey = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.CONSUMER_KEY, null, configuration);
		this.consumerSecret = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.CONSUMER_SECRET, null, configuration);
		this.accessToken = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.ACCESS_TOKEN, null, configuration);
		this.accessTokenSecret = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.ACCESS_TOKEN_SECRET, null,
				configuration);
		this.queueName = ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.QUEUE_NAME, null, configuration);
		this.postOffice = postOffice;
	}

	/**
	 * TODO streaming API support TODO rate limit support
	 */
	public synchronized void start() throws Exception {
		if (this.isStarted) {
			return;
		}

		if (this.connectorName == null || this.connectorName.trim().equals("")) {
			throw new Exception("invalid connector name: " + this.connectorName);
		}

		if (this.queueName == null || this.queueName.trim().equals("")) {
			throw new Exception("invalid queue name: " + queueName);
		}

		SimpleString name = new SimpleString(this.queueName);
		Binding b = this.postOffice.getBinding(name);
		if (b == null) {
			throw new Exception(connectorName + ": queue " + queueName
					+ " not found");
		}
		this.queue = (Queue) b.getBindable();

		TwitterFactory tf = new TwitterFactory();
		this.twitter = tf.getOAuthAuthorizedInstance(this.consumerKey,
				this.consumerSecret, new AccessToken(this.accessToken,
						this.accessTokenSecret));
		this.twitter.verifyCredentials();

		// TODO make filter-string configurable
		// this.filter = FilterImpl.createFilter(filterString);
		this.filter = null;

		this.queue.addConsumer(this);

		this.queue.deliverAsync();
		this.isStarted = true;
		log.debug(connectorName + ": started");
	}

	public boolean isStarted() {
		return isStarted; // To change body of implemented methods use File |
							// Settings | File Templates.
	}

	public synchronized void stop() throws Exception {
		if (!this.isStarted) {
			return;
		}

		log.debug(connectorName + ": receive shutdown request");

		this.queue.removeConsumer(this);

		this.isStarted = false;
		log.debug(connectorName + ": shutdown");
	}

	public String getName() {
		return connectorName;
	}

	public Filter getFilter() {
		return filter;
	}

	public HandleStatus handle(final MessageReference ref) throws Exception {
		if (filter != null && !filter.match(ref.getMessage())) {
			return HandleStatus.NO_MATCH;
		}

		synchronized (this) {
			ref.handled();

			ServerMessage message = ref.getMessage();

			StatusUpdate status = new StatusUpdate(message.getBodyBuffer()
					.readString());

			// set optional property

			if (message
					.containsProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID)) {
				status.setInReplyToStatusId(message
						.getLongProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID));
			}

			if (message
					.containsProperty(TwitterConstants.KEY_GEO_LOCATION_LATITUDE)) {
				double geolat = message
						.getDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LATITUDE);
				double geolong = message
						.getDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LONGITUDE);
				status.setLocation(new GeoLocation(geolat, geolong));
			}

			if (message.containsProperty(TwitterConstants.KEY_PLACE_ID)) {
				status.setPlaceId(message
						.getStringProperty(TwitterConstants.KEY_PLACE_ID));
			}

			if (message
					.containsProperty(TwitterConstants.KEY_DISPLAY_COODINATES)) {
				status.setDisplayCoordinates(message
						.getBooleanProperty(TwitterConstants.KEY_DISPLAY_COODINATES));
			}

			// send to Twitter
			try {
				this.twitter.updateStatus(status);
			} catch (TwitterException e) {
				if (e.getStatusCode() == 403) {
					// duplicated message
					log.warn(connectorName
							+ ": HTTP status code = 403: Ignore duplicated message");
					queue.acknowledge(ref);

					return HandleStatus.HANDLED;
				} else {
					throw e;
				}
			}

			queue.acknowledge(ref);
			log.debug(connectorName + ": forwarded to twitter: "
					+ message.getMessageID());
			return HandleStatus.HANDLED;
		}
	}
}