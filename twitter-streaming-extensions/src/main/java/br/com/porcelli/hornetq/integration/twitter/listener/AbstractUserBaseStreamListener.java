package br.com.porcelli.hornetq.integration.twitter.listener;

import org.hornetq.core.postoffice.PostOffice;

import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public abstract class AbstractUserBaseStreamListener extends
        AbstractBaseStreamListener implements UserStreamListener {

    public AbstractUserBaseStreamListener(final PostOffice postOffice,
                                          final String queueName,
                                          final String lastTweetQueueName) {
        super(postOffice, queueName, lastTweetQueueName);
    }

    @Override
    public void onFavorite(final User arg0, final User arg1, final Status arg2) {}

    @Override
    public void onFollow(final User arg0, final User arg1) {}

    @Override
    public void onFriendList(final int[] arg0) {}

    @Override
    public void onBlock(final User arg0, final User arg1) {}

    @Override
    public void onUnblock(final User arg0, final User arg1) {}

    @Override
    public void onUnfavorite(final User arg0, final User arg1, final Status arg2) {}

    @Override
    public void onUnfollow(final User arg0, final User arg1) {}

    @Override
    public void onUserListCreated(final User arg0, final UserList arg1) {}

    @Override
    public void onUserListDestroyed(final User arg0, final UserList arg1) {}

    @Override
    public void onUserListSubscribed(final User arg0, final User arg1, final UserList arg2) {}

    @Override
    public void onUserListUpdated(final User arg0, final UserList arg1) {}
}
