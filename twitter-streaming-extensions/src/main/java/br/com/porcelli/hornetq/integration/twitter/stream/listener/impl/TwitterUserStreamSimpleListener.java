package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.logging.Logger;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserStreamListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractUserBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class TwitterUserStreamSimpleListener extends
        AbstractUserBaseStreamListener implements UserStreamListener {
    private static final Logger log = Logger
                                        .getLogger(TwitterUserStreamSimpleListener.class);

    public TwitterUserStreamSimpleListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
    }

    @Override
    public void onStatus(final Status status) {
        try {
            message.postMessage(status, false);
        } catch (Exception e) {
            log.error("Error on postMessage", e);
        }
    }

    @Override
    public void onDirectMessage(final DirectMessage directMessage) {
        try {
            message.postMessage(directMessage, false);
        } catch (Exception e) {
            log.error("Error on postMessage", e);
        }
    }

    @Override
    public void onRetweet(final User source, final User target, final Status retweetedStatus) {}
}
