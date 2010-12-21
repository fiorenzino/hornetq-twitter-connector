package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public class StatusStreamHandler extends BaseStreamHandler {

    public StatusStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        super(data, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        if (data.getMentionedUsers() != null || data.getHashTags() != null) {
            final FilterQuery fq = new FilterQuery();
            if (data.getUserIds() != null) {
                fq.follow(data.getUserIds());
            }
            if (data.getHashTags() != null) {
                fq.track(data.getHashTags());
            }
            twitterStream.filter(fq);

        } else {
            twitterStream.firehose(1000);
        }
    }

}
