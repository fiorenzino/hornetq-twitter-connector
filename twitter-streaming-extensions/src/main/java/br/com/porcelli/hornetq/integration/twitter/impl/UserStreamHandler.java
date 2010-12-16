package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractUserBaseStreamListener;

public class UserStreamHandler extends
        BaseStreamHandler<AbstractUserBaseStreamListener> {

    public UserStreamHandler(final String connectorName,
                             final Map<String, Object> configuration,
                             final StorageManager storageManager, final PostOffice postOffice) {
        super(connectorName, configuration, storageManager, postOffice);
    }

    @Override
    protected void startStreaming(Long lastTweetId)
        throws TwitterException {
        twitterStream = new TwitterStreamFactory(conf).getInstance();
        for (final Class<? extends AbstractUserBaseStreamListener> activeListener: listeners) {
            final AbstractUserBaseStreamListener newListener =
                buildListenerInstance((Class<AbstractUserBaseStreamListener>) activeListener);
            if (newListener != null) {
                twitterStream.addListener(newListener);
            }
        }

        twitterStream.user();

        if (lastTweetId != null) {
            Twitter twitter = new TwitterFactory(conf).getInstance();

            loadUserTimeline(lastTweetId, twitter);
            loadDirectMessages(lastTweetId, twitter);

            twitter.shutdown();
        }
    }

}
