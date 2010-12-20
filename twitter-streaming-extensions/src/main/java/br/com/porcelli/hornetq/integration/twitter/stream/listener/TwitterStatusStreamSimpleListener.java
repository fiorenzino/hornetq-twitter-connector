package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.server.ServerMessage;

import twitter4j.Status;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageSupport;

public class TwitterStatusStreamSimpleListener extends
        AbstractStatusBaseStreamListener implements StatusListener {

    public TwitterStatusStreamSimpleListener(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void onStatus(final Status status) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), status);
        MessageSupport.postTweet(getPostOffice(), msg, getLastTweetQueueName(), status.getId());
    }
}
