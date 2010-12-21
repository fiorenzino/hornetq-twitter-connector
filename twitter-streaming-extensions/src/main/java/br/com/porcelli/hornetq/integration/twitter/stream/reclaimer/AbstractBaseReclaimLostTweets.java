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
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractBaseReclaimLostTweets {

    protected final TwitterStreamDTO data;
    protected final MessageQueuing   message;

    public AbstractBaseReclaimLostTweets(final TwitterStreamDTO data, final MessageQueuing message) {
        this.data = data;
        this.message = message;
    }

    public abstract void execute(final Twitter twitter)
        throws Exception;

    protected void loadUserTimeline(final Long lastTweetId, final Twitter twitter)
        throws Exception {
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
    }

    protected void loadDirectMessages(final Integer lastDMId, final Twitter twitter)
        throws Exception {
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
    }

    protected void loadQuery(final String query, final Long lastTweetId, final Twitter twitter)
        throws Exception {
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
