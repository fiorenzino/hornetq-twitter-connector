package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ServerMessage;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserStreamListener;

public class TwitterUserStreamSimpleListener extends
		AbstractUserBaseStreamListener implements UserStreamListener {
	private static final Logger log = Logger
			.getLogger(TwitterUserStreamSimpleListener.class);

	public TwitterUserStreamSimpleListener(PostOffice postOffice,
			StorageManager storageManager, String queueName) {
		super(postOffice, storageManager, queueName);
	}

	@Override
	public void onStatus(Status status) {
		ServerMessage msg = buildMessage(status);
		try {
			getPostOffice().route(msg, false);
		} catch (Exception e) {
			log.error("Error on TwitterUserStreamSimpleListener.onStatus", e);
		}
	}

	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		ServerMessage msg = buildMessage(directMessage);
		try {
			getPostOffice().route(msg, false);
		} catch (Exception e) {
			log.error(
					"Error on TwitterUserStreamSimpleListener.onDirectMessage",
					e);
		}
	}

	@Override
	public void onRetweet(User source, User target, Status retweetedStatus) {
	}
}
