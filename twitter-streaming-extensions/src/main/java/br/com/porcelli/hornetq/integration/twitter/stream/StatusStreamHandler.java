package br.com.porcelli.hornetq.integration.twitter.stream;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStreamFactory;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractStatusBaseStreamListener;

public class StatusStreamHandler extends
        BaseStreamHandler<AbstractStatusBaseStreamListener> {

    public StatusStreamHandler(final String connectorName,
                               final Map<String, Object> configuration,
                               final StorageManager storageManager, final PostOffice postOffice) {
        super(connectorName, configuration, storageManager, postOffice);
    }

    @Override
    protected void startStreaming()
        throws TwitterException {
        twitterStream = new TwitterStreamFactory(commonData.getConf()).getInstance();
        for (final Class<? extends AbstractStatusBaseStreamListener> activeListener: listenersSet) {
            final AbstractStatusBaseStreamListener newListener =
                buildListenerInstance((Class<AbstractStatusBaseStreamListener>) activeListener);
            if (newListener != null) {
                twitterStream.addListener(newListener);
            }
        }

        if (commonData.getMentionedUsers() != null || commonData.getHashTags() != null) {
            final FilterQuery fq = new FilterQuery();
            if (commonData.getUserIds() != null) {
                fq.follow(commonData.getUserIds());
            }
            if (commonData.getHashTags() != null) {
                fq.track(commonData.getHashTags());
            }
            twitterStream.filter(fq);

        } else {
            twitterStream.firehose(1000);
        }
    }

}
