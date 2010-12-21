package br.com.porcelli.hornetq.integration.twitter.stream.listener;

import twitter4j.SiteStreamsListener;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;
import br.com.porcelli.hornetq.integration.twitter.data.TwitterStreamDataModel;
import br.com.porcelli.hornetq.integration.twitter.support.MessageSupport;

public abstract class AbstractSiteBaseStreamListener extends
        AbstractBaseStreamListener implements SiteStreamsListener {

    public AbstractSiteBaseStreamListener(final TwitterStreamDataModel dataModel, final MessageSupport message) {
        super(dataModel, message);
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
