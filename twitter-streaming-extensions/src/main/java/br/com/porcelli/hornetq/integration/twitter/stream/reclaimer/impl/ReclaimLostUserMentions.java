package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl;

import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public class ReclaimLostUserMentions extends AbstractBaseReclaimLostTweets {

    public ReclaimLostUserMentions(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
    }

    @Override
    public void execute(final Twitter twitter)
        throws Exception {
        if (getUserScreenName() != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('@').append(getUserScreenName());

            loadQuery(sb.toString(), getLastTweetId(), twitter);
        }
    }
}
