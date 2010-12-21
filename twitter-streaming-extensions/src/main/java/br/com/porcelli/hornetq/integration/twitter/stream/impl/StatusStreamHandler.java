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

import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;

public class StatusStreamHandler extends BaseStreamHandler {

    public StatusStreamHandler(final TwitterStreamDTO data, final TwitterStream twitterStream) {
        super(data, twitterStream);
    }

    @Override
    public void startStream()
        throws TwitterException {
        if (data.getMentionedUsers() != null || data.getHashTags() != null) {
            final FilterQuery fq = new FilterQuery();
            if (data.getUserIds() != null) {
                fq.follow(data.getUserIds());
            }
            if (data.getHashTags() != null) {
                fq.track(data.getHashTags());
            }
            twitterStream.filter(fq);

        } else {
            twitterStream.firehose(1000);
        }
    }

}
