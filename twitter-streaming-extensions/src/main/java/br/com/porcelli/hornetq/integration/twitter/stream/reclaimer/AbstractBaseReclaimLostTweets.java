package br.com.porcelli.hornetq.integration.twitter.stream.reclaimer;

import org.hornetq.core.server.ServerMessage;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageSupport;

public abstract class AbstractBaseReclaimLostTweets {

    protected final TwitterStreamDataModel data;

    public AbstractBaseReclaimLostTweets(final TwitterStreamDataModel dataModel) {
        data = dataModel;
    }

    public abstract void execute(final Twitter twitter)
        throws TwitterException;

    protected void loadUserTimeline(final long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        while (true) {
            final Paging paging = new Paging(page, lastTweetId);
            final ResponseList<Status> rl = twitter.getUserTimeline(paging);
            if (rl.size() == 0) {
                break;
            }
            for (final Status status: rl) {
                final ServerMessage msg = MessageSupport.buildMessage(data.getQueueName(), status);
                MessageSupport.postTweet(data.getPostOffice(), msg, data.getLastTweetQueueName(), status.getId());
            }
            page++;
        }
    }

    protected void loadDirectMessages(final long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        while (true) {
            final Paging paging = new Paging(page, lastTweetId);
            final ResponseList<DirectMessage> rl = twitter.getDirectMessages(paging);
            if (rl.size() == 0) {
                break;
            }
            for (final DirectMessage dm: rl) {
                final ServerMessage msg = MessageSupport.buildMessage(data.getQueueName(), dm);
                MessageSupport.postDirectMessage(data.getPostOffice(), msg, data.getLastTweetQueueName(), dm.getId());
            }
            page++;
        }
    }

    protected void loadQuery(final String query, final Long lastTweetId, final Twitter twitter)
        throws TwitterException {
        int page = 1;
        query: while (true) {
            final Query qry = new Query(query).sinceId(lastTweetId).page(page);
            final QueryResult qr = twitter.search(qry);
            if (qr.getTweets().size() == 0) {
                break query;
            }
            for (final Tweet activeTweet: qr.getTweets()) {
                final ServerMessage msg = MessageSupport.buildMessage(data.getQueueName(), activeTweet);
                MessageSupport
                    .postTweet(data.getPostOffice(), msg, data.getLastTweetQueueName(), activeTweet.getId());
            }
            page++;
        }
    }

    protected Long getLastTweetId() {
        return data.getLastTweetId();
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
