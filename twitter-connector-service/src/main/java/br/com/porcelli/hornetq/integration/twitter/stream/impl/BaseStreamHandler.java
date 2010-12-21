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
package br.com.porcelli.hornetq.integration.twitter.stream.impl;

import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public abstract class BaseStreamHandler {

    protected final TwitterStream    twitterStream;
    protected final TwitterStreamDTO data;
    private boolean                  isStarted = false;

    public BaseStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
        this.data = data;
    }

    public void start()
        throws TwitterException {
        if (twitterStream != null) {
            startStream();
            isStarted = true;
        }
    }

    public abstract void startStream()
        throws TwitterException;

    public void stop() {
        if (!isStarted) { return; }
        if (twitterStream != null) {
            twitterStream.shutdown();
        }
        isStarted = false;
    }

}
