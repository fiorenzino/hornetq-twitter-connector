package br.com.porcelli.hornetq.integration.twitter.impl;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.Binding;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.core.server.MessageReference;
import org.hornetq.core.server.Queue;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractBaseStreamListener;

public abstract class BaseStreamHandler<T extends AbstractBaseStreamListener>
        implements ConnectorService {
    private static final Logger    log       = Logger.getLogger(BaseStreamHandler.class);

    protected final String         connectorName;

    protected final String         queueName;

    protected final String         lastTweetQueueName;

    protected final Configuration  conf;

    protected final StorageManager storageManager;

    protected final PostOffice     postOffice;

    protected final Set<Class<T>>  listeners;

    protected TwitterStream        twitterStream;

    protected boolean              isStarted = false;

    public BaseStreamHandler(final String connectorName,
                             final Map<String, Object> configuration,
                             final StorageManager storageManager, final PostOffice postOffice) {
        this.connectorName = connectorName;

        this.conf = new ConfigurationBuilder()
                .setOAuthConsumerKey(
                        ConfigurationHelper.getStringProperty(
                                InternalTwitterConstants.PROP_CONSUMER_KEY,
                                null, configuration))
                .setOAuthConsumerSecret(
                        ConfigurationHelper.getStringProperty(
                                InternalTwitterConstants.PROP_CONSUMER_SECRET,
                                null, configuration))
                .setOAuthAccessToken(
                        ConfigurationHelper.getStringProperty(
                                InternalTwitterConstants.PROP_ACCESS_TOKEN,
                                null, configuration))
                .setOAuthAccessTokenSecret(
                        ConfigurationHelper
                                .getStringProperty(
                                        InternalTwitterConstants.PROP_ACCESS_TOKEN_SECRET,
                                        null, configuration)).build();

        this.queueName = ConfigurationHelper.getStringProperty(
                InternalTwitterConstants.PROP_QUEUE_NAME, null, configuration);

        this.lastTweetQueueName = ConfigurationHelper.getStringProperty(
                InternalTwitterConstants.PROP_LAST_TWEET_QUEUE_NAME, null,
                configuration);

        this.storageManager = storageManager;
        this.postOffice = postOffice;
        final String listners = ConfigurationHelper.getStringProperty(
                InternalTwitterConstants.PROP_STREAM_LISTENERS, null,
                configuration);
        if (listners == null || listners.trim().length() == 0) {
            this.listeners = null;
        } else {
            this.listeners = getListeners(listners);
        }
    }

    protected Set<Class<T>> getListeners(final String listners) {
        final Set<Class<T>> result = new HashSet<Class<T>>();

        for (final String activeListner: splitProperty(listners)) {
            try {
                final Class<?> clazz = Class.forName(activeListner);
                if (AbstractBaseStreamListener.class.isAssignableFrom(clazz)) {
                    result.add((Class<T>) clazz);
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Listener '" + activeListner + "' not found");
            }
        }
        if (result.size() > 0) { return result; }
        return null;
    }

    protected String[] splitProperty(final String propertyValue) {
        if (propertyValue == null || propertyValue.trim().length() == 0) { return null; }

        return propertyValue.replace(',', ';').replace(':', ';').split(";");
    }

    @Override
    public String getName() {
        return connectorName;
    }

    @Override
    public void start()
        throws Exception {
        final Binding b = postOffice.getBinding(new SimpleString(queueName));
        if (b == null) { throw new Exception(connectorName + ": queue " + queueName
                    + " not found"); }
        if (this.listeners == null) {
            log.warn("There is no Listners, can't start the service.");
            return;
        }

        Long lastTweetId = null;
        if (lastTweetQueueName != null) {
            final Binding lastTweetBinding = postOffice.getBinding(new SimpleString(lastTweetQueueName));
            if (lastTweetBinding != null) {
                final Queue lastTweetQueue = (Queue) lastTweetBinding.getBindable();
                if (lastTweetQueue.getMessageCount() > 0) {
                    for (Iterator<MessageReference> iterator = lastTweetQueue.iterator(); iterator.hasNext();) {
                        final MessageReference msg = iterator.next();
                        lastTweetId = msg.getMessage().getBodyBuffer().readLong() + 1L;
                    }
                }
            }
        }

        startStreaming(lastTweetId);

        isStarted = true;
    }

    protected abstract void startStreaming(Long lastTweetId)
        throws TwitterException;

    protected void loadDirectMessages(final long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        while (true) {
            Paging paging = new Paging(page, lastTweetId);
            ResponseList<DirectMessage> rl = twitter.getDirectMessages(paging);
            if (rl.size() == 0) {
                break;
            }
            for (DirectMessage dm: rl) {
                ServerMessage msg = MessageSupport.buildMessage(queueName, dm);
                MessageSupport.postDirectMessage(postOffice, msg, lastTweetQueueName, dm.getId());
            }
            page++;
        }
    }

    protected void loadQuery(final String query, final Long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        query: while (true) {
            final Query qry = new Query(query).sinceId(lastTweetId).page(page);
            QueryResult qr = twitter.search(qry);
            if (qr.getTweets().size() == 0) {
                break query;
            }
            for (Tweet activeTweet: qr.getTweets()) {
                ServerMessage msg = MessageSupport.buildMessage(queueName, activeTweet);
                MessageSupport.postTweet(postOffice, msg, lastTweetQueueName, activeTweet.getId());
            }
            page++;
        }
    }

    protected void loadUserTimeline(final long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        while (true) {
            Paging paging = new Paging(page, lastTweetId);
            ResponseList<Status> rl = twitter.getUserTimeline(paging);
            if (rl.size() == 0) {
                break;
            }
            for (Status status: rl) {
                ServerMessage msg = MessageSupport.buildMessage(queueName, status);
                MessageSupport.postTweet(postOffice, msg, lastTweetQueueName, status.getId());
            }
            page++;
        }
    }

    public T buildListenerInstance(final Class<T> clazz) {
        T listener = null;
        final Class<?>[] constructorArgs = new Class[] {PostOffice.class, String.class, String.class};
        final Object[] args = new Object[] {this.postOffice, this.queueName, this.lastTweetQueueName};
        Constructor<T> constructor;

        try {
            constructor = clazz.getConstructor(constructorArgs);
            listener = constructor.newInstance(args);
        } catch (final Exception e) {
            log.error("Listener '");
        }
        return listener;
    }

    @Override
    public void stop()
        throws Exception {
        if (!isStarted) { return; }
        this.twitterStream.shutdown();
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
