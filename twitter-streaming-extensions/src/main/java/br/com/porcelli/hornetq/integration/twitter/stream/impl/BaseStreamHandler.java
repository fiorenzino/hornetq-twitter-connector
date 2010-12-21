package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public abstract class BaseStreamHandler {

    protected final TwitterStream    twitterStream;
    protected final TwitterStreamDTO data;
    private boolean                  isStarted = false;

    public BaseStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
        this.data = data;
    }

    public void start()
        throws TwitterException {
        if (twitterStream != null) {
            startStream();
            isStarted = true;
        }
    }

    public abstract void startStream()
        throws TwitterException;

    public void stop() {
        if (!isStarted) { return; }
        if (twitterStream != null) {
            twitterStream.shutdown();
        }
        isStarted = false;
    }

}
