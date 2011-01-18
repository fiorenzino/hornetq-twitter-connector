package br.com.porcelli.hornetq.integration.twitter.stream.jmx;

import br.com.porcelli.hornetq.integration.twitter.jmx.BaseExceptionNotifierImpl;
import br.com.porcelli.hornetq.integration.twitter.stream.StreamHandler;

public class TwitterStreamManagement extends BaseExceptionNotifierImpl implements
    TwitterStreamManagementMBean {

    private final StreamHandler streamHandler;

    public TwitterStreamManagement(StreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }

    @Override
    public long getDMCount() {
        return streamHandler.getDMCount();
    }

    @Override
    public long getStatusCount() {
        return streamHandler.getStatusCount();
    }

    @Override
    public long getTweetCount() {
        return streamHandler.getTweetCount();
    }

    @Override
    public long getTotalCount() {
        return streamHandler.getTotalCount();
    }

    @Override
    public void start()
        throws Exception {
        streamHandler.start();
    }

    @Override
    public void stop()
        throws Exception {
        streamHandler.stop();
    }

    @Override
    public void restart()
        throws Exception {
        streamHandler.stop();
        streamHandler.start();
    }

}
