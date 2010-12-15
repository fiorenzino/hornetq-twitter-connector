package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public abstract class AbstractStatusBaseStreamListener extends
		AbstractBaseStreamListener implements StatusListener {

	public AbstractStatusBaseStreamListener(PostOffice postOffice,
			StorageManager storageManager, String queueName) {
		super(postOffice, storageManager, queueName);
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
	}

}
