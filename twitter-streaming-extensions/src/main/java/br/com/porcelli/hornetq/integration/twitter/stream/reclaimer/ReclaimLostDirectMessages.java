package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class ReclaimLostDirectMessages extends AbstractBaseReclaimLostTweets {

    public ReclaimLostDirectMessages(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void execute(final Twitter twitter)
        throws TwitterException {
        loadDirectMessages(getLastTweetId(), twitter);
    }

}
