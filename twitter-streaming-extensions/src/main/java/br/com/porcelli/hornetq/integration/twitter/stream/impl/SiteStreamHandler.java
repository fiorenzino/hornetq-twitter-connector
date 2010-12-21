package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public class SiteStreamHandler extends BaseStreamHandler {

    public SiteStreamHandler(TwitterStreamDataModel commonData, TwitterStream twitterStream) {
        super(commonData, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        if (commonData.getUserIds() != null) {
            twitterStream.site(false, commonData.getUserIds());
        } else {
            twitterStream.site(false, null);
        }
    }

}
