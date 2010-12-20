package br.com.porcelli.hornetq.integration.twitter.stream;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractSiteBaseStreamListener;

public class SiteStreamHandler extends
        BaseStreamHandler<AbstractSiteBaseStreamListener> {

    public SiteStreamHandler(final String connectorName,
                               final Map<String, Object> configuration,
                               final StorageManager storageManager, final PostOffice postOffice) {
        super(connectorName, configuration, storageManager, postOffice);
    }

    @Override
    protected void startStreaming()
        throws TwitterException {

        twitterStream = new TwitterStreamFactory(commonData.getConf()).getInstance();
        for (final Class<? extends AbstractSiteBaseStreamListener> activeListener: listenersSet) {
            final AbstractSiteBaseStreamListener newListener =
                buildListenerInstance((Class<AbstractSiteBaseStreamListener>) activeListener);
            if (newListener != null) {
                twitterStream.addListener(newListener);
            }
        }

        if (commonData.getUserIds() != null) {
            twitterStream.site(false, commonData.getUserIds());
        } else {
            twitterStream.site(false, null);
        }
    }

}
