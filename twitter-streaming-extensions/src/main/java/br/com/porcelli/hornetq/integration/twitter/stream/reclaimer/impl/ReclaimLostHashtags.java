package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostHashtags extends AbstractBaseReclaimLostTweets {

    public ReclaimLostHashtags(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
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
