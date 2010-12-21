package br.com.porcelli.hornetq.integration.twitter.stream.listener.impl;

import org.hornetq.core.logging.Logger;

import twitter4j.DirectMessage;
import twitter4j.SiteStreamsListener;
import twitter4j.Status;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractSiteBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class TwitterSiteStreamSimpleListener extends
        AbstractSiteBaseStreamListener implements SiteStreamsListener {
    private static final Logger log = Logger
                                        .getLogger(TwitterSiteStreamSimpleListener.class);

    public TwitterSiteStreamSimpleListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
    }

    @Override
    public void onStatus(final int forUser, final Status status) {
        try {
            message.postMessage(status, false);
        } catch (Exception e) {
            log.error("Error on postMessage", e);
        }
    }

    @Override
    public void onDirectMessage(final int forUser, final DirectMessage directMessage) {
        try {
            message.postMessage(directMessage, false);
        } catch (Exception e) {
            log.error("Error on postMessage", e);
        }
    }

}
