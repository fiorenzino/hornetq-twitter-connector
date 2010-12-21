package br.com.porcelli.hornetq.integration.twitter.stream;

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
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.impl.BaseStreamHandler;
import br.com.porcelli.hornetq.integration.twitter.stream.impl.SiteStreamHandler;
import br.com.porcelli.hornetq.integration.twitter.stream.impl.StatusStreamHandler;
import br.com.porcelli.hornetq.integration.twitter.stream.impl.UserStreamHandler;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractSiteBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractStatusBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.stream.listener.AbstractUserBaseStreamListener;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;
import br.com.porcelli.hornetq.integration.twitter.support.ReflectionSupport;

public final class StreamHandler implements ConnectorService {
    private static final Logger          log                            = Logger.getLogger(StreamHandler.class);

    private final String                 connectorName;

    private final TwitterStreamDataModel commonData;

    private final MessageSupport         message;

    private final Set<BaseStreamHandler> streamHandlers;

    private final Class<?>[]             streamListenersConstructorArgs = new Class<?>[] {TwitterStreamDataModel.class,
                                                                        MessageSupport.class};
    private final Object[]               streamListenersInsanceArgs;

    private boolean                      isStarted                      = false;

    public StreamHandler(final String connectorName, final Map<String, Object> configuration,
                         final StorageManager storageManager, final PostOffice postOffice) {

        this.connectorName = connectorName;

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

        final String lastDMQueueName = ConfigurationHelper.getStringProperty(
            InternalTwitterConstants.PROP_LAST_DM_QUEUE_NAME, null,
            configuration);

        Long lastTweetId = null;
        if (lastTweetQueueName != null) {
            final Binding lastTweetBinding = postOffice.getBinding(new SimpleString(lastTweetQueueName));
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

        Integer lastDMId = null;
        if (lastDMQueueName != null) {
            final Binding lastTweetBinding = postOffice.getBinding(new SimpleString(lastDMQueueName));
            if (lastTweetBinding != null) {
                final Queue lastDMQueue = (Queue) lastTweetBinding.getBindable();
                if (lastDMQueue.getMessageCount() > 0) {
                    for (final Iterator<MessageReference> iterator = lastDMQueue.iterator(); iterator.hasNext();) {
                        final MessageReference msg = iterator.next();
                        lastDMId = msg.getMessage().getBodyBuffer().readInt() + 1;
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
                twitter = new TwitterFactory(conf).getInstance();
                userId = twitter.getId();
                userIds = userIds(twitter.lookupUsers(mentionedUsers));
            } catch (final TwitterException e) {
                e.printStackTrace();
            } finally {
                if (twitter != null) {
                    twitter.shutdown();
                }
            }
        }

        this.commonData =
            new TwitterStreamDataModel(queueName, userScreenName, userId, lastTweetQueueName, lastDMQueueName, lastTweetId,
                lastDMId, mentionedUsers, userIds, hashTags, conf, postOffice);

        final String reclaimers = ConfigurationHelper.getStringProperty(
            InternalTwitterConstants.PROP_LOST_TWEET_RECLAIMERS, null,
            configuration);

        this.message = new MessageSupport(this.commonData, splitProperty(reclaimers));

        this.streamListenersInsanceArgs = new Object[] {this.commonData, this.message};

        final String listenerList =
            ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_STREAM_LISTENERS, null, configuration);

        String[] listeners = splitProperty(listenerList);
        if (listeners != null) {
            UserStreamHandler userHandler = buildUserStreamHandler(listeners);
            SiteStreamHandler siteHandler = buildSiteStreamHandler(listeners);
            StatusStreamHandler statusHandler = buildStatusStreamHandler(listeners);
            if (userHandler != null || siteHandler != null || statusHandler != null) {
                this.streamHandlers = new HashSet<BaseStreamHandler>();
                if (userHandler != null) {
                    this.streamHandlers.add(userHandler);
                }
                if (siteHandler != null) {
                    this.streamHandlers.add(siteHandler);
                }
                if (statusHandler != null) {
                    this.streamHandlers.add(statusHandler);
                }
            } else {
                this.streamHandlers = null;
            }
        } else {
            this.streamHandlers = null;
        }
    }

    private <U extends AbstractUserBaseStreamListener> UserStreamHandler buildUserStreamHandler(String[] listeners) {
        Set<U> result = new HashSet<U>();
        for (String listener: listeners) {
            try {
                final Class<?> clazz = Class.forName(listener);
                if (AbstractUserBaseStreamListener.class.isAssignableFrom(clazz)) {
                    result.add(ReflectionSupport.buildInstance((Class<U>) clazz, streamListenersConstructorArgs,
                        streamListenersInsanceArgs));
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Stream '" + listener + "' not found");
            }
        }
        if (result.size() > 0) {
            TwitterStream twitterStream = new TwitterStreamFactory(this.commonData.getConf()).getInstance();
            for (U activeUserListener: result) {
                twitterStream.addListener(activeUserListener);
            }
            return new UserStreamHandler(this.commonData, twitterStream);
        }
        return null;
    }

    private <S extends AbstractSiteBaseStreamListener> SiteStreamHandler buildSiteStreamHandler(String[] listeners) {
        Set<S> result = new HashSet<S>();
        for (String listener: listeners) {
            try {
                final Class<?> clazz = Class.forName(listener);
                if (AbstractSiteBaseStreamListener.class.isAssignableFrom(clazz)) {
                    result.add(ReflectionSupport.buildInstance((Class<S>) clazz, streamListenersConstructorArgs,
                        streamListenersInsanceArgs));
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Stream '" + listener + "' not found");
            }
        }
        if (result.size() > 0) {
            TwitterStream twitterStream = new TwitterStreamFactory(this.commonData.getConf()).getInstance();
            for (S activeUserListener: result) {
                twitterStream.addListener(activeUserListener);
            }
            return new SiteStreamHandler(this.commonData, twitterStream);
        }
        return null;
    }

    private <ST extends AbstractStatusBaseStreamListener> StatusStreamHandler buildStatusStreamHandler(String[] listeners) {
        Set<ST> result = new HashSet<ST>();
        for (String listener: listeners) {
            try {
                final Class<?> clazz = Class.forName(listener);
                if (AbstractStatusBaseStreamListener.class.isAssignableFrom(clazz)) {
                    result.add(ReflectionSupport.buildInstance((Class<ST>) clazz, streamListenersConstructorArgs,
                        streamListenersInsanceArgs));
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Stream '" + listener + "' not found");
            }
        }
        if (result.size() > 0) {
            TwitterStream twitterStream = new TwitterStreamFactory(this.commonData.getConf()).getInstance();
            for (ST activeUserListener: result) {
                twitterStream.addListener(activeUserListener);
            }
            return new StatusStreamHandler(this.commonData, twitterStream);
        }
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
        if (this.streamHandlers == null || streamHandlers.size() < 1) {
            log.error("There is no Listners, can't start the service.");
            return;
        }

        startStreaming();

        isStarted = true;
    }

    protected void startStreaming()
        throws TwitterException {
        if (streamHandlers != null && streamHandlers.size() > 0) {
            for (BaseStreamHandler activeHandler: streamHandlers) {
                activeHandler.start();
            }
        }
    }

    @Override
    public void stop()
        throws Exception {
        if (!isStarted) { return; }
        if (streamHandlers != null && streamHandlers.size() > 0) {
            for (BaseStreamHandler activeHandler: streamHandlers) {
                activeHandler.stop();
            }
        }
        message.dispose();
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
