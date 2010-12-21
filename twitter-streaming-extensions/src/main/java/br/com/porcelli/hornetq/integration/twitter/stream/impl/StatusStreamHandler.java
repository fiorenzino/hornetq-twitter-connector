package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class StatusStreamHandler extends BaseStreamHandler {

    public StatusStreamHandler(TwitterStreamDataModel commonData, TwitterStream twitterStream) {
        super(commonData, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        if (commonData.getMentionedUsers() != null || commonData.getHashTags() != null) {
            final FilterQuery fq = new FilterQuery();
            if (commonData.getUserIds() != null) {
                fq.follow(commonData.getUserIds());
            }
            if (commonData.getHashTags() != null) {
                fq.track(commonData.getHashTags());
            }
            twitterStream.filter(fq);

        } else {
            twitterStream.firehose(1000);
        }
    }

}
