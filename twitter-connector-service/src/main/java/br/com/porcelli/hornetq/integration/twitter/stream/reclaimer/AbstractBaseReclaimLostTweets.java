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
package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractBaseReclaimLostTweets {

    protected final TwitterStreamDTO  data;
    protected final MessageQueuing    message;
    protected final ExceptionNotifier exceptionNotifier;

    public AbstractBaseReclaimLostTweets(final TwitterStreamDTO data, final MessageQueuing message,
                                         final ExceptionNotifier exceptionNotifier) {
        this.data = data;
        this.message = message;
        this.exceptionNotifier = exceptionNotifier;
    }

    public abstract void execute(final Twitter twitter)
        throws Exception;

    protected void loadUserTimeline(final Long lastTweetId, final Twitter twitter)
        throws Exception {
        try {
            if (lastTweetId == null) { return; }
            int page = 1;
            while (true) {
                final Paging paging = new Paging(page, lastTweetId);
                final ResponseList<Status> rl = twitter.getUserTimeline(paging);
                if (rl.size() == 0) {
                    break;
                }
                for (final Status status: rl) {
                    message.postMessage(status, true);
                }
                page++;
            }
        } catch (Exception e) {
            exceptionNotifier.notifyException(e);
        }
    }

    protected void loadDirectMessages(final Integer lastDMId, final Twitter twitter)
        throws Exception {
        try {
            if (lastDMId == null) { return; }
            int page = 1;
            while (true) {
                final Paging paging = new Paging(page, (long) lastDMId);
                final ResponseList<DirectMessage> rl = twitter.getDirectMessages(paging);
                if (rl.size() == 0) {
                    break;
                }
                for (final DirectMessage dm: rl) {
                    message.postMessage(dm, true);
                }
                page++;
            }
        } catch (Exception e) {
            exceptionNotifier.notifyException(e);
        }
    }

    protected void loadQuery(final String query, final Long lastTweetId, final Twitter twitter)
        throws Exception {
        try {
            if (lastTweetId == null) { return; }
            int page = 1;
            while (true) {
                final Query qry = new Query(query).sinceId(lastTweetId).page(page);
                final QueryResult qr = twitter.search(qry);
                if (qr.getTweets().size() == 0) {
                    break;
                }
                for (final Tweet activeTweet: qr.getTweets()) {
                    message.postMessage(activeTweet, true);
                }
                page++;
            }
        } catch (Exception e) {
            exceptionNotifier.notifyException(e);
        }
    }

    protected Long getLastTweetId() {
        return data.getLastTweetId();
    }

    protected Integer getLastDMId() {
        return data.getLastDMId();
    }

    protected String getUserScreenName() {
        return data.getUserScreenName();
    }

    public String[] getMentionedUsers() {
        return data.getMentionedUsers();
    }

    public String[] getHashTags() {
        return data.getHashTags();
    }

}
