package br.com.porcelli.hornetq.integration.twitter.impl;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.core.server.impl.ServerMessageImpl;

import twitter4j.DirectMessage;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.User;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants.MessageType;

public class TwitterUserStreamDefaultListener extends
		AbstractTwitterUserStreamListener {
	private static final Logger log = Logger
			.getLogger(TwitterUserStreamDefaultListener.class);

	private final PostOffice postOffice;
	private final StorageManager storageManager;
	private final String queueName;

	public TwitterUserStreamDefaultListener(PostOffice postOffice,
			StorageManager storageManager, String queueName) {
		this.postOffice = postOffice;
		this.storageManager = storageManager;
		this.queueName = queueName;
	}

	@Override
	public void onStatus(Status status) {
		ServerMessage msg = buildMessage(status);
		try {
			this.postOffice.route(msg, false);
		} catch (Exception e) {
			log.error("Error on TwitterUserStreamDefaultListener.onStatus", e);
		}
	}

	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		ServerMessage msg = buildMessage(directMessage);
		try {
			this.postOffice.route(msg, false);
		} catch (Exception e) {
			log.error("Error on TwitterUserStreamDefaultListener.onDirectMessage", e);
		}
	}

	@Override
	public void onException(Exception ex) {
		log.error("Got TwitterUserStreamDefaultListener.onException", ex);
	}

	@Override
	public void onRetweet(User source, User target, Status retweetedStatus) {
	}

	private ServerMessage buildMessage(final Status status) {

		final ServerMessage msg = new ServerMessageImpl(
				this.storageManager.generateUniqueID(),
				InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
		msg.setAddress(new SimpleString(this.queueName));
		msg.setDurable(true);
		msg.encodeMessageIDToBuffer();

		msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE,
				MessageType.TWEET.toString());

		msg.getBodyBuffer().writeString(status.getText());
		msg.putLongProperty(TwitterConstants.KEY_ID, status.getId());
		msg.putStringProperty(TwitterConstants.KEY_CONTENT, status.getText());
		msg.putStringProperty(TwitterConstants.KEY_SOURCE, status.getSource());

		msg.putIntProperty(TwitterConstants.KEY_USER_ID, status.getUser()
				.getId());
		msg.putStringProperty(TwitterConstants.KEY_USER_NAME, status.getUser()
				.getName());
		msg.putStringProperty(TwitterConstants.KEY_USER_SCREEN_NAME, status
				.getUser().getScreenName());

		msg.putLongProperty(TwitterConstants.KEY_CREATED_AT, status
				.getCreatedAt().getTime());
		msg.putLongProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID,
				status.getInReplyToStatusId());
		msg.putIntProperty(TwitterConstants.KEY_IN_REPLY_TO_USER_ID,
				status.getInReplyToUserId());
		msg.putBooleanProperty(TwitterConstants.KEY_IS_RETWEET,
				status.isRetweet());

		GeoLocation gl;
		if ((gl = status.getGeoLocation()) != null) {
			msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LATITUDE,
					gl.getLatitude());
			msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LONGITUDE,
					gl.getLongitude());
		}
		Place place;
		if ((place = status.getPlace()) != null) {
			msg.putStringProperty(TwitterConstants.KEY_PLACE_ID, place.getId());
			msg.putStringProperty(TwitterConstants.KEY_PLACE_COUNTRY_CODE,
					place.getCountryCode());
			msg.putStringProperty(TwitterConstants.KEY_PLACE_FULL_NAME,
					place.getFullName());
		}

		return msg;
	}

	private ServerMessage buildMessage(final DirectMessage dm) {

		final ServerMessage msg = new ServerMessageImpl(
				this.storageManager.generateUniqueID(),
				InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
		msg.setAddress(new SimpleString(this.queueName));
		msg.setDurable(true);
		msg.encodeMessageIDToBuffer();

		msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE,
				MessageType.DM.toString());

		msg.getBodyBuffer().writeString(dm.getText());
		msg.putLongProperty(TwitterConstants.KEY_ID, dm.getId());
		msg.putStringProperty(TwitterConstants.KEY_CONTENT, dm.getText());

		msg.putIntProperty(TwitterConstants.KEY_USER_ID, dm.getSender().getId());
		msg.putStringProperty(TwitterConstants.KEY_USER_NAME, dm.getSender()
				.getName());
		msg.putStringProperty(TwitterConstants.KEY_USER_SCREEN_NAME, dm
				.getSender().getScreenName());
		msg.putLongProperty(TwitterConstants.KEY_CREATED_AT, dm.getCreatedAt()
				.getTime());

		return msg;
	}

}
