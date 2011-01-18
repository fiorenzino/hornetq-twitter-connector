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
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractSiteBaseStreamListener extends
        AbstractBaseStreamListener implements SiteStreamsListener {

    public AbstractSiteBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message,
                                          final ExceptionNotifier exceptionNotifier) {
        super(data, message, exceptionNotifier);
    }

    @Override
    public void onBlock(final int forUser, final User source, final User blockedUser) {}

    @Override
    public void onFavorite(final int forUser, final User source, final User target, final Status favoritedStatus) {}

    @Override
    public void onFollow(final int forUser, final User source, final User followedUser) {}

    @Override
    public void onFriendList(final int forUser, final int[] friendIds) {}

    @Override
    public void onUnblock(final int forUser, final User source, final User unblockedUser) {}

    @Override
    public void onUnfavorite(final int forUser, final User source, final User target, final Status unfavoritedStatus) {}

    @Override
    public void onDeletionNotice(final int forUser, final StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onDeletionNotice(final int forUser, final int directMessageId, final int userId) {}

    @Override
    public void onUserListCreation(final int forUser, final User listOwner, final UserList list) {}

    @Override
    public void onUserListDeletion(final int forUser, final User listOwner, final UserList list) {}

    @Override
    public void onUserListSubscription(final int forUser, final User subscriber, final User listOwner, final UserList list) {}

    @Override
    public void onUserListUpdate(final int forUser, final User listOwner, final UserList list) {}

    @Override
    public void onUserProfileUpdate(final int forUser, final User updatedUser) {}

}
