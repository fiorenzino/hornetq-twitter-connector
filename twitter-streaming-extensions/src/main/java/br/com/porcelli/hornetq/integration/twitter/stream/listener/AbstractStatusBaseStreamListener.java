package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public abstract class AbstractStatusBaseStreamListener extends
        AbstractBaseStreamListener implements StatusListener {

    public AbstractStatusBaseStreamListener(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void onDeletionNotice(final StatusDeletionNotice arg0) {}

    @Override
    public void onTrackLimitationNotice(final int arg0) {}

}
