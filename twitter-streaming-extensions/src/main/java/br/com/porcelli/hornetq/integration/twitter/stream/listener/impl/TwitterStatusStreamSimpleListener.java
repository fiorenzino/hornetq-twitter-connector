package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.logging.Logger;

import twitter4j.Status;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractStatusBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class TwitterStatusStreamSimpleListener extends
        AbstractStatusBaseStreamListener implements StatusListener {
    private static final Logger log = Logger
                                        .getLogger(TwitterStatusStreamSimpleListener.class);

    public TwitterStatusStreamSimpleListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
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
}
