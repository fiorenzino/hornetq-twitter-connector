package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.PostOffice;

import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;

public abstract class AbstractBaseStreamListener {
    private static final Logger          log = Logger
                                                 .getLogger(AbstractBaseStreamListener.class);

    private final TwitterStreamDataModel dataModel;

    public AbstractBaseStreamListener(final TwitterStreamDataModel dataModel) {
        this.dataModel = dataModel;
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
