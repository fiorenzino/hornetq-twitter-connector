package br.com.porcelli.hornetq.integration.twitter.stream;

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
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;

public abstract class BaseStreamHandler<T extends AbstractBaseStreamListener>
        implements ConnectorService {
    private static final Logger                                  log       = Logger.getLogger(BaseStreamHandler.class);

    protected final String                                       connectorName;

    protected TwitterStreamDataModel                             commonData;

    protected final Set<Class<T>>                                listenersSet;

    protected TwitterStream                                      twitterStream;

    protected boolean                                            isStarted = false;

    protected final Set<? extends AbstractBaseReclaimLostTweets> reclaimersSet;

    public BaseStreamHandler(final String connectorName,
                             final Map<String, Object> configuration,
                             final StorageManager storageManager, final PostOffice postOffice) {
        this.connectorName = connectorName;

        final String listeners = ConfigurationHelper.getStringProperty(
            InternalTwitterConstants.PROP_STREAM_LISTENERS, null,
            configuration);
        if (listeners == null || listeners.trim().length() == 0) {
            this.listenersSet = null;
        } else {
            this.listenersSet = getListeners(listeners);
        }

        final String reclaimers = ConfigurationHelper.getStringProperty(
            InternalTwitterConstants.PROP_STREAM_LISTENERS, null,
            configuration);
        if (reclaimers == null || reclaimers.trim().length() == 0) {
            this.reclaimersSet = null;
        } else {
            this.reclaimersSet = getReclaimers(reclaimers);
        }

        final Configuration conf = new ConfigurationBuilder()
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

        final String queueName = ConfigurationHelper.getStringProperty(
                InternalTwitterConstants.PROP_QUEUE_NAME, null, configuration);

        final String userScreenName = ConfigurationHelper.getStringProperty(
            InternalTwitterConstants.PROP_SCREEN_NAME, null,
            configuration);

        final String lastTweetQueueName = ConfigurationHelper.getStringProperty(
                InternalTwitterConstants.PROP_LAST_TWEET_QUEUE_NAME, null,
                configuration);

        Long lastTweetId = null;
        if (commonData.getLastTweetQueueName() != null) {
            final Binding lastTweetBinding =
                commonData.getPostOffice().getBinding(new SimpleString(commonData.getLastTweetQueueName()));
            if (lastTweetBinding != null) {
                final Queue lastTweetQueue = (Queue) lastTweetBinding.getBindable();
                if (lastTweetQueue.getMessageCount() > 0) {
                    for (final Iterator<MessageReference> iterator = lastTweetQueue.iterator(); iterator.hasNext();) {
                        final MessageReference msg = iterator.next();
                        lastTweetId = msg.getMessage().getBodyBuffer().readLong() + 1L;
                    }
                }
            }
        }

        final String[] mentionedUsers = splitProperty(ConfigurationHelper
            .getStringProperty(
                    InternalTwitterConstants.PROP_MENTIONED_USERS, null,
                    configuration));

        final String[] hashTags =
            splitProperty(ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_HASHTAGS, null, configuration));

        int[] userIds = null;
        int userId = -1;
        Twitter twitter = null;
        if (mentionedUsers != null) {
            try {
                twitter = new TwitterFactory(commonData.getConf()).getInstance();
                userId = twitter.getId();
                userIds = userIds(twitter.lookupUsers(commonData.getMentionedUsers()));
            } catch (final TwitterException e) {
                e.printStackTrace();
            } finally {
                if (twitter != null) {
                    twitter.shutdown();
                }
            }
        }

        this.commonData =
            new TwitterStreamDataModel(queueName, userScreenName, userId, lastTweetQueueName, lastTweetId, mentionedUsers,
                userIds, hashTags, conf, postOffice);
    }

    private <R extends AbstractBaseReclaimLostTweets> Set<R> getReclaimers(final String reclaimers) {

        final Set<R> result = new HashSet<R>();
        for (final String activeReclaimer: splitProperty(reclaimers)) {
            try {
                final Class<R> clazz = (Class<R>) Class.forName(activeReclaimer);
                if (AbstractBaseReclaimLostTweets.class.isAssignableFrom(clazz)) {
                    result.add(buildReclaiberInstance(clazz));
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Reclaimer '" + activeReclaimer + "' not found");
            }
        }
        if (result.size() > 0) { return result; }
        return null;
    }

    public <R extends AbstractBaseReclaimLostTweets> R buildReclaiberInstance(final Class<R> clazz) {
        R reclaimer = null;
        final Class<?>[] constructorArgs = new Class<?>[] {TwitterStreamDataModel.class};
        final Object[] args = new Object[] {this.commonData};
        Constructor<R> constructor;

        try {
            constructor = clazz.getConstructor(constructorArgs);
            reclaimer = constructor.newInstance(args);
        } catch (final Exception e) {
            log.error("Reclaimer '");
        }
        return reclaimer;
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

    private int[] userIds(final ResponseList<User> users) {
        if (users == null || users.size() == 0) { return new int[0]; }
        final int[] ids = new int[users.size()];
        for (int i = 0; i < users.size(); i++) {
            ids[i] = users.get(i).getId();
        }
        return ids;
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
        final Binding b = commonData.getPostOffice().getBinding(new SimpleString(commonData.getQueueName()));
        if (b == null) { throw new Exception(connectorName + ": queue " + commonData.getQueueName()
                    + " not found"); }
        if (this.listenersSet == null) {
            log.error("There is no Listners, can't start the service.");
            return;
        }

        startStreaming();
        if (commonData.getLastTweetId() != null && reclaimersSet != null) {
            final Twitter twitter = new TwitterFactory(commonData.getConf()).getInstance();
            for (AbstractBaseReclaimLostTweets reclaimer: reclaimersSet) {
                reclaimer.execute(twitter);
                reclaimer = null;
            }
            reclaimersSet.clear();
            twitter.shutdown();
        }

        isStarted = true;
    }

    protected abstract void startStreaming()
        throws TwitterException;

    public T buildListenerInstance(final Class<T> clazz) {
        T listener = null;
        final Class<?>[] constructorArgs = new Class[] {TwitterStreamDataModel.class};
        final Object[] args = new Object[] {this.commonData};
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
