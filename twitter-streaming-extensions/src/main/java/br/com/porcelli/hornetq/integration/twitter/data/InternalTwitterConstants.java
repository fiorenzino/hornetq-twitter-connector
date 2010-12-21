package br.com.porcelli.hornetq.integration.twitter.data;

import java.util.HashSet;
import java.util.Set;

import org.hornetq.api.core.SimpleString;

import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;

/**
 * A TwitterConstants
 * 
 * @author <a href="tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 */
public class InternalTwitterConstants implements TwitterConstants {

    public static final SimpleString LAST_TWEET_ID_VALUE           = new SimpleString("last_tweet");
    public static final SimpleString LAST_DM_ID_VALUE              = new SimpleString("last_dm");

    public static final int          DEFAULT_POLLING_INTERVAL_SECS = 10;
    public static final int          DEFAULT_PAGE_SIZE             = 100;
    public static final int          FIRST_ATTEMPT_PAGE_SIZE       = 1;
    public static final int          START_SINCE_ID                = 1;
    public static final int          INITIAL_MESSAGE_BUFFER_SIZE   = 70;

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
    public static final String       PROP_LOST_TWEET_RECLAIMERS    = "lostTweetReclaimers";

    // FILTER
    public static final String       PROP_MENTIONED_USERS          = "mentionedUsers";
    public static final String       PROP_HASHTAGS                 = "hashtags";

    static {
        ALLOWABLE_STREAM_CONNECTOR_KEYS = new HashSet<String>();
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_STREAM_LISTENERS);
        ALLOWABLE_STREAM_CONNECTOR_KEYS.add(PROP_LOST_TWEET_RECLAIMERS);
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

        REQUIRED_OUTGOING_CONNECTOR_KEYS = new HashSet<String>();
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_KEY);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_CONSUMER_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_ACCESS_TOKEN_SECRET);
        REQUIRED_OUTGOING_CONNECTOR_KEYS.add(PROP_QUEUE_NAME);
    }
}
