package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostMentionedList extends AbstractBaseReclaimLostTweets {

    public ReclaimLostMentionedList(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void execute(final Twitter twitter)
        throws TwitterException {
        if (getMentionedUsers() != null) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getMentionedUsers().length; i++) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append('@').append(getMentionedUsers()[i]);
            }
            loadQuery(sb.toString(), getLastTweetId(), twitter);
        }
    }

}
