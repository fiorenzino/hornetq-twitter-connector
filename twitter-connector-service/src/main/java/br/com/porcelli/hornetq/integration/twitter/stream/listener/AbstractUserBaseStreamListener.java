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

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDTO;
import br.com.porcelli.hornetq.integration.twitter.jmx.ExceptionNotifier;
import br.com.porcelli.hornetq.integration.twitter.stream.MessageQueuing;

public abstract class AbstractUserBaseStreamListener extends
        AbstractBaseStreamListener implements UserStreamListener {

    public AbstractUserBaseStreamListener(final TwitterStreamDTO data, final MessageQueuing message,
                                          final ExceptionNotifier exceptionNotifier) {
        super(data, message, exceptionNotifier);
    }

    @Override
    public void onFavorite(final User source, final User target, final Status favoritedStatus) {}

    @Override
    public void onFollow(final User source, final User followedUser) {}

    @Override
    public void onFriendList(final int[] friendIds) {}

    @Override
    public void onBlock(final User source, final User blockedUser) {}

    @Override
    public void onUnblock(final User source, final User unblockedUser) {}

    @Override
    public void onUnfavorite(final User source, final User target, final Status unfavoritedStatus) {}

    @Override
    public void onDeletionNotice(final StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onScrubGeo(final int userId, final long upToStatusId) {}

    @Override
    public void onTrackLimitationNotice(final int numberOfLimitedStatuses) {}

    @Override
    public void onDeletionNotice(final long directMessageId, final int userId) {}

    @Override
    public void onUserListCreation(final User listOwner, final UserList list) {}

    @Override
    public void onUserListDeletion(final User listOwner, final UserList list) {}

    @Override
    public void onUserListSubscription(final User subscriber, final User listOwner, final UserList list) {}

    @Override
    public void onUserListUpdate(final User listOwner, final UserList list) {}

    @Override
    public void onUserProfileUpdate(final User updatedUser) {}

    @Override
    public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {}

    @Override
    public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {}

    @Override
    public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {}

}
