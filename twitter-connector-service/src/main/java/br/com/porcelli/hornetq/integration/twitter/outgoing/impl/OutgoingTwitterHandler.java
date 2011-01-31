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
package br.com.porcelli.hornetq.integration.twitter.outgoing.impl;

import static br.com.porcelli.hornetq.integration.twitter.support.TweetMessageConverterSupport.buildMessage;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.hornetq.api.core.PropertyConversionException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.core.filter.Filter;
import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.Binding;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.core.server.Consumer;
import org.hornetq.core.server.HandleStatus;
import org.hornetq.core.server.MessageReference;
import org.hornetq.core.server.Queue;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.DirectMessage;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants.MessageType;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.outgoing.jmx.TwitterOutgoingManagement;

/**
 * OutgoingTwitterHandler consumes from configured HornetQ address and forwards to the twitter.
 */
public class OutgoingTwitterHandler implements Consumer, ConnectorService {
    private static final Logger             log                        = Logger
                                                                              .getLogger(OutgoingTwitterHandler.class);

    private final String                    connectorName;

    private final PostOffice                postOffice;

    private final Queue                     errorQueue;

    private final Queue                     sentQueue;

    private final Queue                     queue;

    private final Twitter                   twitter;

    private boolean                         isStarted                  = false;

    private final AtomicLong                tweetSent                  = new AtomicLong();
    private final AtomicLong                dmSent                     = new AtomicLong();
    private final AtomicLong                totalSent                  = new AtomicLong();

    private final TwitterOutgoingManagement mbean;

    private final String                    ERROR_MESSAGE_NOT_FILLED   = "aa";
    private final String                    ERROR_MESSAGE_SIZE_EXCEED  = "bb";
    private final String                    ERROR_INVALID_MESSAGE_TYPE = "cc";
    private final String                    ERROR_DM_DESTINY_NOT_FOUND = "dd";

    public OutgoingTwitterHandler(final String connectorName,
                                  final Map<String, Object> configuration, final PostOffice postOffice) {

        this.connectorName = connectorName;

        this.mbean = new TwitterOutgoingManagement(this);

        try {
            MBeanServer mbServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName mbeanName = new ObjectName("org.hornetq:module=ConnectorService,name=" + connectorName);
            mbServer.registerMBean(mbean, mbeanName);
        } catch (Exception e) {
            log.error("Error on registering JMX info.", e);
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

        this.postOffice = postOffice;
        this.twitter = new TwitterFactory(conf).getInstance();

        final String queueName =
            ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_QUEUE_NAME, null, configuration);

        final String errorQueueName =
            ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_ERROR_QUEUE_NAME, null, configuration);

        final String sentQueueName =
            ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_SENT_QUEUE_NAME, null, configuration);

        final Binding queueBinding = postOffice.getBinding(new SimpleString(queueName));
        if (queueBinding == null) { throw new RuntimeException(connectorName + ": queue " + queueName + " not found"); }
        queue = (Queue) queueBinding.getBindable();

        if (errorQueueName != null && errorQueueName.trim().length() > 0) {
            final Binding errorQueueBinding = postOffice.getBinding(new SimpleString(errorQueueName));
            if (errorQueueBinding == null) { throw new RuntimeException(connectorName + ": queue " + errorQueueName
                + " not found"); }
            errorQueue = (Queue) errorQueueBinding.getBindable();
        } else {
            errorQueue = null;
        }

        if (sentQueueName != null && sentQueueName.trim().length() > 0) {
            final Binding sentQueueBinding = postOffice.getBinding(new SimpleString(sentQueueName));
            if (sentQueueBinding == null) { throw new RuntimeException(connectorName + ": queue " + sentQueueName + " not found"); }
            sentQueue = (Queue) sentQueueBinding.getBindable();
        } else {
            sentQueue = null;
        }
    }

    @Override
    public synchronized void start()
        throws Exception {
        if (isStarted) { return; }

        queue.addConsumer(this);

        queue.deliverAsync();
        isStarted = true;
        log.debug(connectorName + ": started");
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public synchronized void stop()
        throws Exception {
        if (!isStarted) { return; }

        log.debug(connectorName + ": receive shutdown request");

        queue.removeConsumer(this);

        isStarted = false;
        log.debug(connectorName + ": shutdown");
    }

    @Override
    public String getName() {
        return connectorName;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public HandleStatus handle(final MessageReference ref) {
        System.out.println("Entrando no handle()");
        synchronized (this) {
            ref.handled();
            final ServerMessage message = ref.getMessage();
            try {
                String text2publish;
                if (message.containsProperty(TwitterConstants.KEY_TEXT)) {
                    text2publish = message.getStringProperty(TwitterConstants.KEY_TEXT);
                } else {
                    text2publish = message.getBodyBuffer().readString();
                }

                if (text2publish == null || text2publish.trim().length() <= 0) {
                    log.error(ERROR_MESSAGE_NOT_FILLED);
                    throw new Exception(ERROR_MESSAGE_NOT_FILLED);
                } else if (text2publish.length() > 140) {
                    log.warn(ERROR_MESSAGE_SIZE_EXCEED);
                    text2publish = text2publish.substring(0, 139);
                }

                MessageType type = MessageType.TWEET;
                try {
                    if (message.containsProperty(TwitterConstants.KEY_MSG_TYPE)) {
                        type = MessageType.valueOf(message.getStringProperty(TwitterConstants.KEY_MSG_TYPE).toUpperCase());
                    }
                } catch (Exception e) {
                    log.warn(ERROR_INVALID_MESSAGE_TYPE);
                }

                if (type == MessageType.DM) {
                    if (!message.containsProperty(TwitterConstants.KEY_TO_USER_ID)
                        && !message.containsProperty(TwitterConstants.KEY_TO_USER_SCREEN_NAME)) {
                        log.error(ERROR_DM_DESTINY_NOT_FOUND);
                        throw new Exception(ERROR_DM_DESTINY_NOT_FOUND);
                    }

                    DirectMessage sentMessage = null;
                    if (message.containsProperty(TwitterConstants.KEY_TO_USER_ID)) {
                        int userId = -1;
                        try {
                            userId = message.getIntProperty(TwitterConstants.KEY_TO_USER_ID);
                        } catch (PropertyConversionException e) {
                            userId = Integer.valueOf(message.getStringProperty(TwitterConstants.KEY_TO_USER_ID));
                        }
                        sentMessage = twitter.sendDirectMessage(userId, text2publish);
                        dmSent.incrementAndGet();
                        totalSent.incrementAndGet();
                    } else if (message.containsProperty(TwitterConstants.KEY_TO_USER_SCREEN_NAME)) {
                        String userScreenName = message.getStringProperty(TwitterConstants.KEY_TO_USER_SCREEN_NAME);
                        sentMessage = twitter.sendDirectMessage(userScreenName, text2publish);
                        dmSent.incrementAndGet();
                        totalSent.incrementAndGet();
                    }
                    if (sentMessage != null && sentQueue != null) {
                        final ServerMessage msg = buildMessage(sentQueue.getName().toString(), sentMessage);
                        msg.setAddress(sentQueue.getName());
                        msg.setDurable(true);
                        postOffice.route(msg, false);
                    }
                } else {
                    final StatusUpdate status = new StatusUpdate(text2publish);

                    if (message.containsProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID)) {
                        long reply2StatusId = 0L;
                        try {
                            reply2StatusId = message.getLongProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID);
                        } catch (PropertyConversionException e) {
                            reply2StatusId = Long.valueOf(message.getStringProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID));
                        }
                        status.setInReplyToStatusId(reply2StatusId);
                    }

                    if (message.containsProperty(TwitterConstants.KEY_GEO_LATITUDE)
                        && message.containsProperty(TwitterConstants.KEY_GEO_LONGITUDE)) {
                        double geolat = 0.0D;
                        double geolong = 0.0D;
                        try {
                            geolat = message.getDoubleProperty(TwitterConstants.KEY_GEO_LATITUDE);
                        } catch (PropertyConversionException e) {
                            geolat = Double.valueOf(message.getStringProperty(TwitterConstants.KEY_GEO_LATITUDE));
                        }
                        try {
                            geolong = message.getDoubleProperty(TwitterConstants.KEY_GEO_LONGITUDE);
                        } catch (PropertyConversionException e) {
                            geolong = Double.valueOf(message.getStringProperty(TwitterConstants.KEY_GEO_LONGITUDE));
                        }

                        status.setLocation(new GeoLocation(geolat, geolong));
                    }

                    if (message.containsProperty(TwitterConstants.KEY_PLACE_ID)) {
                        status.setPlaceId(message.getStringProperty(TwitterConstants.KEY_PLACE_ID));
                    }

                    if (message.containsProperty(TwitterConstants.KEY_DISPLAY_COODINATES)) {
                        boolean displayCoordinated = false;
                        try {
                            displayCoordinated = message.getBooleanProperty(TwitterConstants.KEY_DISPLAY_COODINATES);
                        } catch (PropertyConversionException e) {
                            displayCoordinated =
                                Boolean.valueOf(message.getStringProperty(TwitterConstants.KEY_DISPLAY_COODINATES));
                        }
                        status.setDisplayCoordinates(displayCoordinated);
                    }
                    Status sentMessage = twitter.updateStatus(status);
                    tweetSent.incrementAndGet();
                    totalSent.incrementAndGet();
                    if (sentMessage != null && sentQueue != null) {
                        final ServerMessage msg = buildMessage(sentQueue.getName().toString(), sentMessage);
                        msg.setAddress(sentQueue.getName());
                        msg.setDurable(true);
                        postOffice.route(msg, false);
                    }
                }
            } catch (Exception e) {
                mbean.notifyException(e);
                log.error("Error sending message.", e);
                if (errorQueue != null) {
                    final ServerMessage msg = message.copy();
                    msg.setAddress(errorQueue.getName());
                    msg.setDurable(true);
                    try {
                        postOffice.route(msg, false);
                    } catch (Exception e1) {
                        mbean.notifyException(e1);
                    }
                }
            }
            try {
                queue.acknowledge(ref);
            } catch (Exception e) {
                mbean.notifyException(e);
                log.error("Error acknowledging message.", e);
            }
            return HandleStatus.HANDLED;
        }
    }

    public long getDMSent() {
        return dmSent.get();
    }

    public long getTweetSent() {
        return tweetSent.get();
    }

    public long getTotalSent() {
        return totalSent.get();
    }
}
