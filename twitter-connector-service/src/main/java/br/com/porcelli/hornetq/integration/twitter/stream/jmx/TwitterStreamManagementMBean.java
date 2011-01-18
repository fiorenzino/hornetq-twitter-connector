package br.com.porcelli.hornetq.integration.twitter.stream.jmx;

import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;

public interface TwitterStreamManagementMBean extends ExceptionNotifier {

    public long getDMCount();

    public long getStatusCount();

    public long getTweetCount();

    public long getTotalCount();

    public void start()
        throws Exception;

    public void stop()
        throws Exception;

    public void restart()
        throws Exception;
}
