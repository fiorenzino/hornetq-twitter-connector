package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ServerMessage;

import twitter4j.Status;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.impl.MessageSupport;

public class TwitterStatusStreamSimpleListener extends
        AbstractStatusBaseStreamListener implements StatusListener {

    public TwitterStatusStreamSimpleListener(final PostOffice postOffice,
                                             final String queueName,
                                             final String lastTweetQueueName) {
        super(postOffice, queueName, lastTweetQueueName);
    }

    @Override
    public void onStatus(final Status status) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), status);
        MessageSupport.postTweet(getPostOffice(), msg, getLastTweetQueueName(), status.getId());
    }
}
