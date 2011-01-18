/**
 * Copyright (c) 2010 Alexandre Porcelli <alexandre.porcelli@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import org.hornetq.core.logging.Logger;
import org.hornetq.core.postoffice.PostOffice;

import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractBaseStreamListener {
    private static final Logger       log = Logger
                                                 .getLogger(AbstractBaseStreamListener.class);

    private final TwitterStreamDTO    data;
    protected final ExceptionNotifier exceptionNotifier;
    protected final MessageQueuing    message;

    public AbstractBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message,
                                      final ExceptionNotifier exceptionNotifier) {
        this.data = data;
        this.message = message;
        this.exceptionNotifier = exceptionNotifier;
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
        exceptionNotifier.notifyException(ex);
        log.error("Got AbstractBaseStreamListener.onException", ex);
    }

}
