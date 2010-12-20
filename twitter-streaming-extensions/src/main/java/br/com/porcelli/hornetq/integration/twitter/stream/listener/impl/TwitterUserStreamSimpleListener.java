package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.server.ServerMessage;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserStreamListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractUserBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class TwitterUserStreamSimpleListener extends
        AbstractUserBaseStreamListener implements UserStreamListener {

    public TwitterUserStreamSimpleListener(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void onStatus(final Status status) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), status);
        MessageSupport.postTweet(getPostOffice(), msg, getLastTweetQueueName(), status.getId());
    }

    @Override
    public void onDirectMessage(final DirectMessage directMessage) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), directMessage);
        MessageSupport.postDirectMessage(getPostOffice(), msg, getLastTweetQueueName(), directMessage.getId());
    }

    @Override
    public void onRetweet(final User source, final User target, final Status retweetedStatus) {}
}
