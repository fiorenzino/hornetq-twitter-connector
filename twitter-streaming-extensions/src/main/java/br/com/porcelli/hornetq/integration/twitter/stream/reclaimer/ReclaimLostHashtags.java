package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class ReclaimLostHashtags extends AbstractBaseReclaimLostTweets {

    public ReclaimLostHashtags(final TwitterStreamDataModel dataModel) {
        super(dataModel);
    }

    @Override
    public void execute(final Twitter twitter)
        throws TwitterException {
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
