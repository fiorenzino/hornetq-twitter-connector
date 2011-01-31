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

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.postoffice.PostOffice;

import twitter4j.conf.Configuration;

public class TwitterStreamDTO {
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
    private final Long          lastDMId;
    private final String[]      mentionedUsers;
    private final int[]         userIds;
    private final String[]      hashTags;

    public TwitterStreamDTO(final String queueName,
                                  final String userScreenName,
                                  final int userId,
                                  final String lastTweetQueueName,
                                  final String lastDMQueueName,
                                  final Long lastTweetId,
                                  final Long lastDMId,
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
            lastFormattedTweetQueueName = new SimpleString(lastTweetQueueName);
        } else {
            lastFormattedTweetQueueName = null;
        }
        if (lastDMQueueName != null) {
            lastFormattedDMQueueName = new SimpleString(lastDMQueueName);
        } else {
            lastFormattedDMQueueName = null;
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

    public Long getLastDMId() {
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
