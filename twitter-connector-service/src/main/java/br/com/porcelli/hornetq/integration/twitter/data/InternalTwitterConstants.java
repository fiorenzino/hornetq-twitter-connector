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
package br.com.porcelli.hornetq.integration.twitter.data;

import java.util.HashSet;
import java.util.Set;

import org.hornetq.api.core.SimpleString;

public class InternalTwitterConstants {

    public static final SimpleString LAST_TWEET_ID_VALUE           = new SimpleString("last_tweet");
    public static final SimpleString LAST_DM_ID_VALUE              = new SimpleString("last_dm");

    public static final int          DEFAULT_POLLING_INTERVAL_SECS = 10;
    public static final int          DEFAULT_PAGE_SIZE             = 100;
    public static final int          FIRST_ATTEMPT_PAGE_SIZE       = 1;
    public static final int          START_SINCE_ID                = 1;
    public static final int          INITIAL_MESSAGE_BUFFER_SIZE   = 256;

    public static final Set<String>  ALLOWABLE_STREAM_CONNECTOR_KEYS;
    public static final Set<String>  REQUIRED_STREAM_CONNECTOR_KEYS;

    public static final Set<String>  ALLOWABLE_OUTGOING_CONNECTOR_KEYS;
    public static final Set<String>  REQUIRED_OUTGOING_CONNECTOR_KEYS;

    public static final String       PROP_CONSUMER_KEY             = "consumerKey";
    public static final String       PROP_CONSUMER_SECRET          = "consumerSecret";
    public static final String       PROP_ACCESS_TOKEN             = "accessToken";
    public static final String       PROP_ACCESS_TOKEN_SECRET      = "accessTokenSecret";
    public static final String       PROP_QUEUE_NAME               = "queue";
    public static final String       PROP_SCREEN_NAME              = "screenName";

    public static final String       PROP_LAST_TWEET_QUEUE_NAME    = "lastTweetQueue";
    public static final String       PROP_LAST_DM_QUEUE_NAME       = "lastDMQueue";

    // STREAM
    public static final String       PROP_STREAM_LISTENERS         = "streamListeners";
    public static final String       PROP_TWEET_RECLAIMERS         = "tweetReclaimers";

    // FILTER
    public static final String       PROP_MENTIONED_USERS          = "mentionedUsers";
    public static final String       PROP_HASHTAGS                 = "hashtags";

    //Output
    public static final String       PROP_ERROR_QUEUE_NAME         = "errorQueue";
    public static final String       PROP_SENT_QUEUE_NAME          = "sentQueue";

    public static final String       KEY_USER_SENDER_PREFIX        = "sender_";
    public static final String       KEY_USER_RECIPIENT_PREFIX     = "recipient_";

    static {
        ALLOWABLE_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_STREAM_LISTENERS);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_TWEET_RECLAIMERS);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_SCREEN_NAME);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_LAST_TWEET_QUEUE_NAME);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_LAST_DM_QUEUE_NAME);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_HASHTAGS);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_MENTIONED_USERS);

        REQUIRED_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);

        ALLOWABLE_OUTGOING_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_ERROR_QUEUE_NAME);
        ALLOWABLE_OUTGOING_CONNECTOR_KEYS.add(PROP_SENT_QUEUE_NAME);

        REQUIRED_OUTGOING_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
    }
}
