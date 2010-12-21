package br.com.porcelli.hornetq.integration.twitter.data;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.conf.Configuration;

public class TwitterStreamDataModel {
    private final String        queueName;
    private final String        userScreenName;
    private final int           userId;
    private final String        lastTweetQueueName;
    private final SimpleString  lastFormattedTweetQueueName;
    private final String        lastDMQueueName;
    private final SimpleString  lastFormattedDMQueueName;
    private final Configuration conf;
    private final PostOffice    postOffice;
    private final Long          lastTweetId;
    private final Integer       lastDMId;
    private final String[]      mentionedUsers;
    private final int[]         userIds;
    private final String[]      hashTags;

    public TwitterStreamDataModel(final String queueName,
                                  final String userScreenName,
                                  final int userId,
                                  final String lastTweetQueueName,
                                  final String lastDMQueueName,
                                  final Long lastTweetId,
                                  final Integer lastDMId,
                                  final String[] mentionedUsers,
                                  final int[] userIds,
                                  final String[] hashTags,
                                  final Configuration conf,
                                  final PostOffice postOffice) {
        this.queueName = queueName;
        this.userScreenName = userScreenName;
        this.userId = userId;
        this.lastTweetQueueName = lastTweetQueueName;
        this.lastDMQueueName = lastDMQueueName;
        this.lastTweetId = lastTweetId;
        this.lastDMId = lastDMId;
        this.mentionedUsers = mentionedUsers;
        this.userIds = userIds;
        this.hashTags = hashTags;
        this.conf = conf;
        this.postOffice = postOffice;
        if (lastTweetQueueName != null) {
            this.lastFormattedTweetQueueName = new SimpleString(lastTweetQueueName);
        } else {
            this.lastFormattedTweetQueueName = null;
        }
        if (lastDMQueueName != null) {
            this.lastFormattedDMQueueName = new SimpleString(lastDMQueueName);
        } else {
            this.lastFormattedDMQueueName = null;
        }

    }

    public String getQueueName() {
        return queueName;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public int getUserId() {
        return userId;
    }

    public String getLastTweetQueueName() {
        return lastTweetQueueName;
    }

    public SimpleString getFormattedLastTweetQueueName() {
        return lastFormattedTweetQueueName;
    }

    public String getLastDMQueueName() {
        return lastDMQueueName;
    }

    public SimpleString getFormattedLastDMQueueName() {
        return lastFormattedDMQueueName;
    }

    public Long getLastTweetId() {
        return lastTweetId;
    }

    public Integer getLastDMId() {
        return lastDMId;
    }

    public String[] getMentionedUsers() {
        return mentionedUsers;
    }

    public int[] getUserIds() {
        return userIds;
    }

    public String[] getHashTags() {
        return hashTags;
    }

    public Configuration getConf() {
        return conf;
    }

    public PostOffice getPostOffice() {
        return postOffice;
    }

}
