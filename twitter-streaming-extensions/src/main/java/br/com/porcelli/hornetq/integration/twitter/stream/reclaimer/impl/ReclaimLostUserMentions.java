package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostUserMentions extends AbstractBaseReclaimLostTweets {

    public ReclaimLostUserMentions(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void execute(final Twitter twitter)
        throws TwitterException {
        if (getUserScreenName() != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('@').append(getUserScreenName());

            loadQuery(sb.toString(), getLastTweetId(), twitter);
        }
    }
}
