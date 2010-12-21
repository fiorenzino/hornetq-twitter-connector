package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.PostOffice;

import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractBaseStreamListener {
    private static final Logger    log = Logger
                                                 .getLogger(AbstractBaseStreamListener.class);

    private final TwitterStreamDTO data;
    protected final MessageQueuing message;

    public AbstractBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message) {
        this.data = data;
        this.message = message;
    }

    public PostOffice getPostOffice() {
        return data.getPostOffice();
    }

    public String getQueueName() {
        return data.getQueueName();
    }

    public String getLastTweetQueueName() {
        return data.getLastTweetQueueName();
    }

    public int[] getUserIds() {
        return data.getUserIds();
    }

    public void onException(final Exception ex) {
        log.error("Got AbstractBaseStreamListener.onException", ex);
    }

}
