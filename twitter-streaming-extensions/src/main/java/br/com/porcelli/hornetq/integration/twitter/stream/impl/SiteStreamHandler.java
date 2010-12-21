package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public class SiteStreamHandler extends BaseStreamHandler {

    public SiteStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        super(data, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        if (data.getUserIds() != null) {
            twitterStream.site(false, data.getUserIds());
        } else {
            twitterStream.site(false, null);
        }
    }

}
