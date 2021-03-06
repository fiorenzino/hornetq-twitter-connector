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
