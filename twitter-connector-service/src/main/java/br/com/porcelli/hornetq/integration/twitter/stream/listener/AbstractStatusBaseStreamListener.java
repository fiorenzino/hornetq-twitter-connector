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

import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractStatusBaseStreamListener extends
        AbstractBaseStreamListener implements StatusListener {

    public AbstractStatusBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message,
                                            final ExceptionNotifier exceptionNotifier) {
        super(data, message, exceptionNotifier);
    }

    @Override
    public void onDeletionNotice(final StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onTrackLimitationNotice(final int numberOfLimitedStatuses) {}

    @Override
    public void onScrubGeo(final int userId, final long upToStatusId) {}

}
