package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.logging.Logger;

import twitter4j.Status;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractStatusBaseStreamListener;

public class TwitterStatusStreamSimpleListener extends
        AbstractStatusBaseStreamListener implements StatusListener {
    private static final Logger log = Logger
                                        .getLogger(TwitterStatusStreamSimpleListener.class);

    public TwitterStatusStreamSimpleListener(final TwitterStreamDTO data, final MessageQueuing message) {
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
}
