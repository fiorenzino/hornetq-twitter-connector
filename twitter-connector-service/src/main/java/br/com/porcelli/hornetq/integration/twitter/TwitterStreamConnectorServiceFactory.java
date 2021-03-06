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
package br.com.porcelli.hornetq.integration.twitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.ConnectorService;
import org.hornetq.core.server.ConnectorServiceFactory;

import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.stream.StreamHandler;

public class TwitterStreamConnectorServiceFactory implements ConnectorServiceFactory {

    @Override
    public ConnectorService createConnectorService(final String connectorName, final Map<String, Object> configuration,
                                                   final StorageManager storageManager, final PostOffice postOffice,
                                                   final ScheduledExecutorService scheduledThreadPool) {
        return new StreamHandler(connectorName, configuration, storageManager, postOffice);
    }

    @Override
    public Set<String> getAllowableProperties() {
        return InternalTwitterConstants.ALLOWABLE_STREAM_CONNECTOR_KEYS;
    }

    @Override
    public Set<String> getRequiredProperties() {
        return InternalTwitterConstants.REQUIRED_STREAM_CONNECTOR_KEYS;
    }
}
