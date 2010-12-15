package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ServerMessage;

import twitter4j.Status;
import twitter4j.StatusListener;

public class TwitterStatusStreamSimpleListener extends
		AbstractStatusBaseStreamListener implements StatusListener {
	private static final Logger log = Logger
			.getLogger(TwitterStatusStreamSimpleListener.class);

	public TwitterStatusStreamSimpleListener(PostOffice postOffice,
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

}
