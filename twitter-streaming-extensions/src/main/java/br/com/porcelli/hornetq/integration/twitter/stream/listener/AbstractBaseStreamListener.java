package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.PostOffice;

import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public abstract class AbstractBaseStreamListener {
    private static final Logger          log = Logger
                                                 .getLogger(AbstractBaseStreamListener.class);

    private final TwitterStreamDataModel dataModel;
    protected final MessageSupport       message;

    public AbstractBaseStreamListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        this.dataModel = dataModel;
        this.message = message;
    }

    public PostOffice getPostOffice() {
        return dataModel.getPostOffice();
    }

    public String getQueueName() {
        return dataModel.getQueueName();
    }

    public String getLastTweetQueueName() {
        return dataModel.getLastTweetQueueName();
    }

    public int[] getUserIds() {
        return dataModel.getUserIds();
    }

    public void onException(final Exception ex) {
        log.error("Got AbstractBaseStreamListener.onException", ex);
    }

}
