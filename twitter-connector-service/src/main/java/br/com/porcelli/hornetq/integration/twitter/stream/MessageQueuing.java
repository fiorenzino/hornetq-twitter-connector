/**
 * Copyright (c) 2010 Alexandre Porcelli <alexandre.porcelli@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.porcelli.hornetq.integration.twitter.stream;

import static br.com.porcelli.hornetq.integration.twitter.support.TweetMessageConverterSupport.buildMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.Message;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.ServerMessage;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.AbstractBaseReclaimLostTweets;
import br.com.porcelli.hornetq.integration.twitter.support.ReflectionSupport;

public class MessageQueuing {
    private static final Logger                                log               = Logger.getLogger(MessageQueuing.class);
    private final TwitterStreamDTO                             data;
    private final Set<? extends AbstractBaseReclaimLostTweets> reclaimersSet;
    private final ExceptionNotifier                            exceptionNotifier;
    private ClientProducer                                     producerLastTweet = null;
    private ClientProducer                                     producerLastDM    = null;
    private ClientSession                                      session           = null;
    private final AtomicLong                                   tweetCount        = new AtomicLong();
    private final AtomicLong                                   dmCount           = new AtomicLong();
    private final AtomicLong                                   statusCount       = new AtomicLong();
    private final AtomicLong                                   totalCount        = new AtomicLong();

    public MessageQueuing(final TwitterStreamDTO data, final ExceptionNotifier exceptionNotifier, final String[] reclaimers) {
        this.data = data;
        this.exceptionNotifier = exceptionNotifier;
        if (reclaimers == null) {
            reclaimersSet = null;
        } else {
            reclaimersSet = buildReclaimers(this.data, reclaimers);
        }
    }

    private <R extends AbstractBaseReclaimLostTweets> Set<R> buildReclaimers(final TwitterStreamDTO data,
                                                                             final String[] reclaimers) {
        final Class<?>[] constructorArgs = new Class<?>[] {TwitterStreamDTO.class, MessageQueuing.class, ExceptionNotifier.class};
        final Object[] args = new Object[] {data, this, exceptionNotifier};

        final Set<R> result = new HashSet<R>();
        for (final String activeReclaimer: reclaimers) {
            try {
                final Class<R> clazz = (Class<R>) Class.forName(activeReclaimer);
                if (AbstractBaseReclaimLostTweets.class.isAssignableFrom(clazz)) {
                    result.add(ReflectionSupport.buildInstance(clazz, constructorArgs, args));
                }
            } catch (final ClassNotFoundException e) {
                exceptionNotifier.notifyException(e);
                log.error("Twitter Reclaimer '" + activeReclaimer + "' not found");
            }
        }
        if (result.size() == 0) { return null; }
        return result;
    }

    public void postMessage(final Status status, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), status);
        statusCount.incrementAndGet();
        totalCount.incrementAndGet();
        internalPostTweet(msg, status.getId(), isReclaimer);
    }

    public void postMessage(final Tweet tweet, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), tweet);
        tweetCount.incrementAndGet();
        totalCount.incrementAndGet();
        internalPostTweet(msg, tweet.getId(), isReclaimer);
    }

    public void postMessage(final DirectMessage dm, final boolean isReclaimer)
        throws Exception {
        final ServerMessage msg = buildMessage(data.getQueueName(), dm);
        dmCount.incrementAndGet();
        totalCount.incrementAndGet();
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

    private void internalPostDM(final ServerMessage msg, final long id, final boolean isReclaimer)
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
                exceptionNotifier.notifyException(e);
                log.error("Error on postLastTweetQueue.", e);
            }
        }
    }

    private void postOnLastDMQueue(final long id) {
        if (data.getLastDMQueueName() != null) {
            try {
                if (getSession() != null) {
                    final Message msg = getSession().createMessage(true);
                    msg.setAddress(data.getFormattedLastDMQueueName());
                    msg.getBodyBuffer().writeLong(id);
                    msg.putStringProperty(Message.HDR_LAST_VALUE_NAME, InternalTwitterConstants.LAST_DM_ID_VALUE);
                    producerLastDM.send(msg);
                }
            } catch (final Exception e) {
                exceptionNotifier.notifyException(e);
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
            exceptionNotifier.notifyException(e);
            return null;
        }
    }

    public void dispose() {
        if (producerLastTweet != null) {
            try {
                producerLastTweet.close();
            } catch (final HornetQException e) {
                exceptionNotifier.notifyException(e);
                log.error("Error on producerLastTweet close.", e);
            }
        }
        if (producerLastDM != null) {
            try {
                producerLastDM.close();
            } catch (final HornetQException e) {
                exceptionNotifier.notifyException(e);
                log.error("Error on producerLastDM close.", e);
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (final HornetQException e) {
                exceptionNotifier.notifyException(e);
                log.error("Error on session close", e);
            }
        }
    }

    public long getDMCount() {
        return dmCount.get();
    }

    public long getStatusCount() {
        return statusCount.get();
    }

    public long getTweetCount() {
        return tweetCount.get();
    }

    public long getTotalCount() {
        return totalCount.get();
    }

}
