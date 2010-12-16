package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractStatusBaseStreamListener;

public class StatusStreamHandler extends
        BaseStreamHandler<AbstractStatusBaseStreamListener> {

    private final String[] mentionedUsers;
    private final String[] hashTags;

    public StatusStreamHandler(final String connectorName,
                               final Map<String, Object> configuration,
                               final StorageManager storageManager, final PostOffice postOffice) {
        super(connectorName, configuration, storageManager, postOffice);

        mentionedUsers = splitProperty(ConfigurationHelper
                .getStringProperty(
                        InternalTwitterConstants.PROP_MENTIONED_USERS, null,
                        configuration));

        hashTags =
            splitProperty(ConfigurationHelper.getStringProperty(InternalTwitterConstants.PROP_HASHTAGS, null, configuration));

    }

    @Override
    protected void startStreaming(Long lastTweetId)
        throws TwitterException {
        twitterStream = new TwitterStreamFactory(conf).getInstance();
        for (final Class<? extends AbstractStatusBaseStreamListener> activeListener: listeners) {
            final AbstractStatusBaseStreamListener newListener =
                buildListenerInstance((Class<AbstractStatusBaseStreamListener>) activeListener);
            if (newListener != null) {
                twitterStream.addListener(newListener);
            }
        }

        if (mentionedUsers != null || hashTags != null) {
            final FilterQuery fq = new FilterQuery();
            if (mentionedUsers != null) {
                final Twitter twitter = new TwitterFactory(conf).getInstance();
                int[] userIds = null;
                try {
                    userIds = userIds(twitter.lookupUsers(mentionedUsers));
                } catch (final TwitterException e) {}
                if (userIds != null) {
                    fq.follow(userIds);
                }
            }
            if (hashTags != null) {
                fq.track(hashTags);
            }
            twitterStream.filter(fq);

            if (lastTweetId != null) {
                StringBuilder sb = new StringBuilder();
                if (hashTags != null) {
                    for (int i = 0; i < hashTags.length; i++) {
                        if (i > 0) {
                            sb.append(" OR ");
                        }
                        sb.append(hashTags[i]);
                    }
                }
                if (mentionedUsers != null) {
                    for (int i = 0; i < mentionedUsers.length; i++) {
                        if (sb.length() > 0) {
                            sb.append(" OR ");
                        }
                        sb.append('@').append(mentionedUsers[i]);
                    }
                }
                Twitter twitter = new TwitterFactory(conf).getInstance();

                loadQuery(sb.toString(), lastTweetId, twitter);
                loadDirectMessages(lastTweetId, twitter);

                twitter.shutdown();
            }

        } else {
            twitterStream.firehose(1000);
        }
    }

    private int[] userIds(final ResponseList<User> users) {
        if (users == null || users.size() == 0) { return new int[0]; }
        final int[] ids = new int[users.size()];
        for (int i = 0; i < users.size(); i++) {
            ids[i] = users.get(i).getId();
        }
        return ids;
    }
}
