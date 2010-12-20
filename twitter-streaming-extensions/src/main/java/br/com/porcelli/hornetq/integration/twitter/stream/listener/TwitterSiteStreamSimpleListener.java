package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.server.ServerMessage;

import twitter4j.DirectMessage;
import twitter4j.SiteStreamsListener;
import twitter4j.Status;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageSupport;

public class TwitterSiteStreamSimpleListener extends
        AbstractSiteBaseStreamListener implements SiteStreamsListener {

    public TwitterSiteStreamSimpleListener(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void onStatus(final int forUser, final Status status) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), status);
        MessageSupport.postTweet(getPostOffice(), msg, getLastTweetQueueName(), status.getId());
    }

    @Override
    public void onDirectMessage(final int forUser, final DirectMessage directMessage) {
        final ServerMessage msg = MessageSupport.buildMessage(getQueueName(), directMessage);
        MessageSupport.postDirectMessage(getPostOffice(), msg, getLastTweetQueueName(), directMessage.getId());
    }

}
