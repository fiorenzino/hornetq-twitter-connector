package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.logging.Logger;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserStreamListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractUserBaseStreamListener;

public class TwitterUserStreamSimpleListener extends
        AbstractUserBaseStreamListener implements UserStreamListener {
    private static final Logger log = Logger
                                        .getLogger(TwitterUserStreamSimpleListener.class);

    public TwitterUserStreamSimpleListener(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
    }

    @Override
    public void onStatus(final Status status) {
        try {
            message.postMessage(status, false);
        } catch (final Exception e) {
            log.error("Error on postMessage", e);
        }
    }

    @Override
    public void onDirectMessage(final DirectMessage directMessage) {
        try {
            message.postMessage(directMessage, false);
        } catch (final Exception e) {
            log.error("Error on postMessage", e);
        }
    }

    @Override
    public void onRetweet(final User source, final User target, final Status retweetedStatus) {}
}
