/*
 * Copyright 2010 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package br.com.porcelli.hornetq.integration.twitter;

import java.util.HashSet;
import java.util.Set;

/**
 * A TwitterConstants
 * 
 * @author <a href="tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 */
public class InternalTwitterConstants implements TwitterConstants {

    public static final String      LAST_ID                       = "last.id";

    public static final int         DEFAULT_POLLING_INTERVAL_SECS = 10;
    public static final int         DEFAULT_PAGE_SIZE             = 100;
    public static final int         FIRST_ATTEMPT_PAGE_SIZE       = 1;
    public static final int         START_SINCE_ID                = 1;
    public static final int         INITIAL_MESSAGE_BUFFER_SIZE   = 70;

    public static final Set<String> ALLOWABLE_USER_STREAM_CONNECTOR_KEYS;
    public static final Set<String> REQUIRED_USER_STREAM_CONNECTOR_KEYS;

    public static final Set<String> ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS;
    public static final Set<String> REQUIRED_STATUS_STREAM_CONNECTOR_KEYS;

    public static final Set<String> ALLOWABLE_OUTGOING_CONNECTOR_KEYS;
    public static final Set<String> REQUIRED_OUTGOING_CONNECTOR_KEYS;

    public static final String      PROP_CONSUMER_KEY             = "consumerKey";
    public static final String      PROP_CONSUMER_SECRET          = "consumerSecret";
    public static final String      PROP_ACCESS_TOKEN             = "accessToken";
    public static final String      PROP_ACCESS_TOKEN_SECRET      = "accessTokenSecret";
    public static final String      PROP_QUEUE_NAME               = "queue";

    public static final String      PROP_LAST_TWEET_QUEUE_NAME    = "lastTweetQueue";

    // STREAM
    public static final String      PROP_STREAM_LISTENERS         = "streamListeners";

    // FILTER
    public static final String      PROP_MENTIONED_USERS          = "mentionedUsers";
    public static final String      PROP_HASHTAGS                 = "hashtags";

    static {
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_STREAM_LISTENERS);
        ALLOWABLE_USER_STREAM_CONNECTOR_KEYS.add(PROP_LAST_TWEET_QUEUE_NAME);

        REQUIRED_USER_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_USER_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_USER_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_USER_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_USER_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_USER_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);

        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_STREAM_LISTENERS);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_MENTIONED_USERS);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_HASHTAGS);
        ALLOWABLE_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_LAST_TWEET_QUEUE_NAME);

        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_STATUS_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);

        ALLOWABLE_OUTGOING_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);

        REQUIRED_OUTGOING_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
    }
}
