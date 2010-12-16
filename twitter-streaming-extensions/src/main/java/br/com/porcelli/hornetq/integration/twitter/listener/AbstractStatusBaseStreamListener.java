package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.postoffice.PostOffice;

import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public abstract class AbstractStatusBaseStreamListener extends
        AbstractBaseStreamListener implements StatusListener {

    public AbstractStatusBaseStreamListener(final PostOffice postOffice,
                                            final String queueName,
                                            final String lastTweetQueueName) {
        super(postOffice, queueName, lastTweetQueueName);
    }

    @Override
    public void onDeletionNotice(final StatusDeletionNotice arg0) {}

    @Override
    public void onTrackLimitationNotice(final int arg0) {}

}
