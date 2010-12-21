package br.com.porcelli.hornetq.integration.twitter.stream;

import java.util.HashSet;
import java.util.Set;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.Message;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.core.server.impl.ServerMessageImpl;

import twitter4j.DirectMessage;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants.MessageType;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;
import br.com.porcelli.hornetq.integration.twitter.support.ReflectionSupport;

public class MessageQueuing {
    private static final Logger                                log               = Logger
                                                                                     .getLogger(MessageQueuing.class);
    private final TwitterStreamDTO                             data;
    private final Set<? extends AbstractBaseReclaimLostTweets> reclaimersSet;
    private ClientProducer                                     producerLastTweet = null;
    private ClientProducer                                     producerLastDM    = null;
    private ClientSession                                      session           = null;

    public MessageQueuing(final TwitterStreamDTO data, final String[] reclaimers) {
        this.data = data;
        if (reclaimers == null) {
            reclaimersSet = null;
        } else {
            reclaimersSet = buildReclaimers(this.data, reclaimers);
        }
    }

    private <R extends AbstractBaseReclaimLostTweets> Set<R> buildReclaimers(final TwitterStreamDTO data,
                                                                             final String[] reclaimers) {
        final Class<?>[] constructorArgs = new Class<?>[] {TwitterStreamDTO.class, MessageQueuing.class};
        final Object[] args = new Object[] {data, this};

        final Set<R> result = new HashSet<R>();
        for (final String activeReclaimer: reclaimers) {
            try {
                final Class<R> clazz = (Class<R>) Class.forName(activeReclaimer);
                if (AbstractBaseReclaimLostTweets.class.isAssignableFrom(clazz)) {
                    result.add(ReflectionSupport.buildInstance(clazz, constructorArgs, args));
                }
            } catch (final ClassNotFoundException e) {
                log.error("Twitter Reclaimer '" + activeReclaimer + "' not found");
            }
        }
        if (result.size() == 0) { return null; }
        return result;
    }

    public void postMessage(final Status status, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), status);
        internalPostTweet(msg, status.getId(), isReclaimer);
    }

    public void postMessage(final Tweet tweet, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), tweet);
        internalPostTweet(msg, tweet.getId(), isReclaimer);
    }

    public void postMessage(final DirectMessage dm, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), dm);
        internalPostDM(msg, dm.getId(), isReclaimer);
    }

    private void internalPostTweet(final ServerMessage msg, final long id, final boolean isReclaimer)
        throws Exception {
        data.getPostOffice().route(msg, false);
        postOnLastTweetQueue(id);
        if (!isReclaimer) {
            executeReclaimers();
        }
    }

    private void internalPostDM(final ServerMessage msg, final int id, final boolean isReclaimer)
        throws Exception {
        data.getPostOffice().route(msg, false);
        postOnLastDMQueue(id);
        if (!isReclaimer) {
            executeReclaimers();
        }
    }

    private void executeReclaimers() {
        if (reclaimersSet != null && reclaimersSet.size() > 0) {
            final Twitter twitter = new TwitterFactory(data.getConf()).getInstance();
            final Set<AbstractBaseReclaimLostTweets> executedReclaimers = new HashSet<AbstractBaseReclaimLostTweets>();
            for (final AbstractBaseReclaimLostTweets reclaimer: reclaimersSet) {
                try {
                    reclaimer.execute(twitter);
                    executedReclaimers.add(reclaimer);
                } catch (final Exception e) {
                    log.error("Couldn't execute reclaimer:" + reclaimer.getClass().getName(), e);
                }
            }
            twitter.shutdown();
            for (AbstractBaseReclaimLostTweets activeExecutedReclaimer: executedReclaimers) {
                reclaimersSet.remove(activeExecutedReclaimer);
                activeExecutedReclaimer = null;
            }
        }
    }

    private void postOnLastTweetQueue(final long id) {
        if (data.getLastTweetQueueName() != null) {
            try {
                if (getSession() != null) {
                    final Message msg = getSession().createMessage(true);
                    msg.setAddress(data.getFormattedLastTweetQueueName());
                    msg.getBodyBuffer().writeLong(id);
                    msg.putStringProperty(Message.HDR_LAST_VALUE_NAME, InternalTwitterConstants.LAST_TWEET_ID_VALUE);
                    producerLastTweet.send(msg);
                }
            } catch (final Exception e) {
                log.error("Error on postLastTweetQueue.", e);
            }
        }
    }

    private void postOnLastDMQueue(final int id) {
        if (data.getLastDMQueueName() != null) {
            try {
                if (getSession() != null) {
                    final Message msg = getSession().createMessage(true);
                    msg.setAddress(data.getFormattedLastDMQueueName());
                    msg.getBodyBuffer().writeInt(id);
                    msg.putStringProperty(Message.HDR_LAST_VALUE_NAME, InternalTwitterConstants.LAST_DM_ID_VALUE);
                    producerLastDM.send(msg);
                }
            } catch (final Exception e) {
                log.error("Error on postLastDMQueue.", e);
            }
        }
    }

    private ClientSession getSession() {
        if (session != null) { return session; }
        if (data.getFormattedLastTweetQueueName() == null && data.getFormattedLastDMQueueName() == null) { return null; }
        try {
            final ClientSessionFactory sf =
                HornetQClient.createClientSessionFactory(new TransportConfiguration(InVMConnectorFactory.class.getName()));
            session = sf.createSession();
            if (data.getFormattedLastTweetQueueName() != null) {
                producerLastTweet = session.createProducer(data.getFormattedLastTweetQueueName());
            }
            if (data.getFormattedLastDMQueueName() != null) {
                producerLastDM = session.createProducer(data.getFormattedLastDMQueueName());
            }
            return session;
        } catch (final HornetQException e) {
            return null;
        }
    }

    private ServerMessage buildMessage(final String queueName, final Status status) {
        final ServerMessage msg =
            new ServerMessageImpl(status.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE, MessageType.TWEET.toString());

        msg.getBodyBuffer().writeString(status.getText());
        msg.putLongProperty(TwitterConstants.KEY_ID, status.getId());
        msg.putStringProperty(TwitterConstants.KEY_CONTENT, status.getText());
        msg.putStringProperty(TwitterConstants.KEY_SOURCE, status.getSource());

        msg.putIntProperty(TwitterConstants.KEY_USER_ID, status.getUser()
                .getId());
        msg.putStringProperty(TwitterConstants.KEY_USER_NAME, status.getUser()
                .getName());
        msg.putStringProperty(TwitterConstants.KEY_USER_SCREEN_NAME, status
                .getUser().getScreenName());

        msg.putLongProperty(TwitterConstants.KEY_CREATED_AT, status
                .getCreatedAt().getTime());
        msg.putLongProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID,
                status.getInReplyToStatusId());
        msg.putIntProperty(TwitterConstants.KEY_IN_REPLY_TO_USER_ID,
                status.getInReplyToUserId());
        msg.putBooleanProperty(TwitterConstants.KEY_IS_RETWEET,
                status.isRetweet());

        GeoLocation gl;
        if ((gl = status.getGeoLocation()) != null) {
            msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LATITUDE,
                    gl.getLatitude());
            msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LONGITUDE,
                    gl.getLongitude());
        }
        Place place;
        if ((place = status.getPlace()) != null) {
            msg.putStringProperty(TwitterConstants.KEY_PLACE_ID, place.getId());
            msg.putStringProperty(TwitterConstants.KEY_PLACE_COUNTRY_CODE,
                    place.getCountryCode());
            msg.putStringProperty(TwitterConstants.KEY_PLACE_FULL_NAME,
                    place.getFullName());
        }

        return msg;
    }

    private ServerMessage buildMessage(final String queueName, final DirectMessage dm) {

        final ServerMessage msg = new ServerMessageImpl(dm.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);
        msg.encodeMessageIDToBuffer();

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE,
                MessageType.DM.toString());

        msg.getBodyBuffer().writeString(dm.getText());
        msg.putLongProperty(TwitterConstants.KEY_ID, dm.getId());
        msg.putStringProperty(TwitterConstants.KEY_CONTENT, dm.getText());

        msg.putIntProperty(TwitterConstants.KEY_USER_ID, dm.getSender().getId());
        msg.putStringProperty(TwitterConstants.KEY_USER_NAME, dm.getSender()
                .getName());
        msg.putStringProperty(TwitterConstants.KEY_USER_SCREEN_NAME, dm
                .getSender().getScreenName());
        msg.putLongProperty(TwitterConstants.KEY_CREATED_AT, dm.getCreatedAt()
                .getTime());

        return msg;
    }

    private ServerMessage buildMessage(final String queueName, final Tweet status) {
        final ServerMessage msg =
            new ServerMessageImpl(status.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);
        msg.encodeMessageIDToBuffer();

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE,
                MessageType.TWEET.toString());

        msg.getBodyBuffer().writeString(status.getText());
        msg.putLongProperty(TwitterConstants.KEY_ID, status.getId());
        msg.putStringProperty(TwitterConstants.KEY_CONTENT, status.getText());
        msg.putStringProperty(TwitterConstants.KEY_SOURCE, status.getSource());

        msg.putIntProperty(TwitterConstants.KEY_USER_ID, status.getFromUserId());
        msg.putStringProperty(TwitterConstants.KEY_USER_NAME, status.getFromUser());

        msg.putLongProperty(TwitterConstants.KEY_CREATED_AT, status.getCreatedAt().getTime());

        GeoLocation gl;
        if ((gl = status.getGeoLocation()) != null) {
            msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LATITUDE,
                    gl.getLatitude());
            msg.putDoubleProperty(TwitterConstants.KEY_GEO_LOCATION_LONGITUDE,
                    gl.getLongitude());
        }

        return msg;
    }

    public void dispose() {
        if (producerLastTweet != null) {
            try {
                producerLastTweet.close();
            } catch (final HornetQException e) {
                log.error("Error on producerLastTweet close.", e);
            }
        }
        if (producerLastDM != null) {
            try {
                producerLastDM.close();
            } catch (final HornetQException e) {
                log.error("Error on producerLastDM close.", e);
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (final HornetQException e) {
                log.error("Error on session close", e);
            }
        }
    }

}
