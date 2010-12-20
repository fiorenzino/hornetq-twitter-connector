package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class ReclaimLostTimeline extends AbstractBaseReclaimLostTweets {

    public ReclaimLostTimeline(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void execute(final Twitter twitter)
        throws TwitterException {
        loadUserTimeline(getLastTweetId(), twitter);
    }

}
