package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public abstract class AbstractStatusBaseStreamListener extends
        AbstractBaseStreamListener implements StatusListener {

    public AbstractStatusBaseStreamListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
    }

    @Override
    public void onDeletionNotice(final StatusDeletionNotice arg0) {}

    @Override
    public void onTrackLimitationNotice(final int arg0) {}

}
