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

import twitter4j.SiteStreamsListener;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractSiteBaseStreamListener extends
        AbstractBaseStreamListener implements SiteStreamsListener {

    public AbstractSiteBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message) {
        super(data, message);
    }

    @Override
    public void onBlock(final int arg0, final User arg1, final User arg2) {}

    @Override
    public void onFavorite(final int arg0, final User arg1, final User arg2, final Status arg3) {}

    @Override
    public void onFollow(final int arg0, final User arg1, final User arg2) {}

    @Override
    public void onFriendList(final int arg0, final int[] arg1) {}

    @Override
    public void onUnblock(final int arg0, final User arg1, final User arg2) {}

    @Override
    public void onUnfavorite(final int arg0, final User arg1, final User arg2, final Status arg3) {}

    @Override
    public void onUserListCreated(final int arg0, final User arg1, final UserList arg2) {}

    @Override
    public void onUserListDestroyed(final int arg0, final User arg1, final UserList arg2) {}

    @Override
    public void onUserListSubscribed(final int arg0, final User arg1, final User arg2, final UserList arg3) {}

    @Override
    public void onUserListUpdated(final int arg0, final User arg1, final UserList arg2) {}

}
