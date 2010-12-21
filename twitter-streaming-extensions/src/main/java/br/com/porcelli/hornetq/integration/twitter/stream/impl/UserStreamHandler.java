package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public class UserStreamHandler extends BaseStreamHandler {

    public UserStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        super(data, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        twitterStream.user();
    }

}
