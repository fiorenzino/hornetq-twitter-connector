package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class ReclaimLostDirectMessages extends AbstractBaseReclaimLostTweets {

    public ReclaimLostDirectMessages(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
    }

    @Override
    public void execute(final Twitter twitter)
        throws Exception {
        loadDirectMessages(getLastDMId(), twitter);
    }

}
