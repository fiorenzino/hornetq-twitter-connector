package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostMentionedList extends AbstractBaseReclaimLostTweets {

    public ReclaimLostMentionedList(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
    }

    @Override
    public void execute(final Twitter twitter)
        throws Exception {
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
