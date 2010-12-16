package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.PostOffice;

public abstract class AbstractBaseStreamListener {
    private static final Logger  log = Logger
                                         .getLogger(AbstractBaseStreamListener.class);

    private final PostOffice     postOffice;
    private final String         queueName;
    private final String         lastTweetQueueName;

    public AbstractBaseStreamListener(final PostOffice postOffice,
                                      final String queueName,
                                      final String lastTweetQueueName) {
        this.postOffice = postOffice;
        this.queueName = queueName;
        this.lastTweetQueueName = lastTweetQueueName;
    }

    public PostOffice getPostOffice() {
        return postOffice;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getLastTweetQueueName() {
        return lastTweetQueueName;
    }

    public void onException(final Exception ex) {
        log.error("Got AbstractBaseStreamListener.onException", ex);
    }

}
