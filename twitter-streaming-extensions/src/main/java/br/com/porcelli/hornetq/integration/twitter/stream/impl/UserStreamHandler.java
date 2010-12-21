package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class UserStreamHandler extends BaseStreamHandler {

    public UserStreamHandler(TwitterStreamDataModel commonData, TwitterStream twitterStream) {
        super(commonData, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        twitterStream.user();
    }

}
