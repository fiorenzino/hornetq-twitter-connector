package br.com.porcelli.hornetq.integration.twitter.stream;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractUserBaseStreamListener;

public class UserStreamHandler extends
        BaseStreamHandler<AbstractUserBaseStreamListener> {

    public UserStreamHandler(final String connectorName,
                             final Map<String, Object> configuration,
                             final StorageManager storageManager, final PostOffice postOffice) {
        super(connectorName, configuration, storageManager, postOffice);
    }

    @Override
    protected void startStreaming()
        throws TwitterException {
        twitterStream = new TwitterStreamFactory(commonData.getConf()).getInstance();
        for (final Class<? extends AbstractUserBaseStreamListener> activeListener: listenersSet) {
            final AbstractUserBaseStreamListener newListener =
                buildListenerInstance((Class<AbstractUserBaseStreamListener>) activeListener);
            if (newListener != null) {
                twitterStream.addListener(newListener);
            }
        }

        twitterStream.user();
    }

}
