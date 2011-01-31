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
package br.com.porcelli.hornetq.integration.twitter.outgoing.jmx;

import br.com.porcelli.hornetq.integration.twitter.jmx.BaseExceptionNotifierImpl;
import br.com.porcelli.hornetq.integration.twitter.outgoing.impl.OutgoingTwitterHandler;

public class TwitterOutgoingManagement extends BaseExceptionNotifierImpl implements
    TwitterOutgoingManagementMBean {

    private final OutgoingTwitterHandler outgoingHandler;

    public TwitterOutgoingManagement(OutgoingTwitterHandler outgoingHandler) {
        this.outgoingHandler = outgoingHandler;
    }

    @Override
    public long getDMSent() {
        return outgoingHandler.getDMSent();
    }

    @Override
    public long getTweetSent() {
        return outgoingHandler.getTweetSent();
    }

    @Override
    public long getTotalSent() {
        return outgoingHandler.getTotalSent();
    }

    @Override
    public void start()
        throws Exception {
        outgoingHandler.start();
    }

    @Override
    public void stop()
        throws Exception {
        outgoingHandler.stop();
    }

    @Override
    public void restart()
        throws Exception {
        outgoingHandler.stop();
        outgoingHandler.start();
    }

}
