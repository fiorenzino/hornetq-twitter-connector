package br.com.porcelli.hornetq.integration.twitter.data;

import org.hornetq.core.postoffice.PostOffice;

import twitter4j.conf.Configuration;

public class TwitterStreamDataModel {
    private final String        queueName;
    private final String        userScreenName;
    private final int           userId;
    private final String        lastTweetQueueName;
    private final Configuration conf;
    private final PostOffice    postOffice;
    private final Long          lastTweetId;
    private final String[]      mentionedUsers;
    private final int[]         userIds;
    private final String[]      hashTags;

    public TwitterStreamDataModel(final String queueName,
                                  final String userScreenName,
                                  final int userId,
                                  final String lastTweetQueueName,
                                  final Long lastTweetId,
                                  final String[] mentionedUsers,
                                  final int[] userIds,
                                  final String[] hashTags,
                                  final Configuration conf,
                                  final PostOffice postOffice) {
        this.queueName = queueName;
        this.userScreenName = userScreenName;
        this.userId = userId;
        this.lastTweetQueueName = lastTweetQueueName;
        this.lastTweetId = lastTweetId;
        this.mentionedUsers = mentionedUsers;
        this.userIds = userIds;
        this.hashTags = hashTags;
        this.conf = conf;
        this.postOffice = postOffice;
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

    public Long getLastTweetId() {
        return lastTweetId;
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
