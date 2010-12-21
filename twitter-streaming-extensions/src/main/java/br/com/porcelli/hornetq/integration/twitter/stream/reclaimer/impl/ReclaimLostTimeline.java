package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostTimeline extends AbstractBaseReclaimLostTweets {

    public ReclaimLostTimeline(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
    }

    @Override
    public void execute(final Twitter twitter)
        throws Exception {
        loadUserTimeline(getLastTweetId(), twitter);
    }

}
