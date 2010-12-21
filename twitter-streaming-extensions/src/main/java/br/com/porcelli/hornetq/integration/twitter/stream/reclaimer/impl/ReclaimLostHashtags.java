package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public class ReclaimLostHashtags extends AbstractBaseReclaimLostTweets {

    public ReclaimLostHashtags(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
    }

    @Override
    public void execute(final Twitter twitter)
        throws Exception {
        if (getHashTags() != null) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < getHashTags().length; i++) {
                if (sb.length() > 0) {
                    sb.append(" OR ");
                }
                sb.append(getMentionedUsers()[i]);
            }
            loadQuery(sb.toString(), getLastTweetId(), twitter);
        }
    }

}
