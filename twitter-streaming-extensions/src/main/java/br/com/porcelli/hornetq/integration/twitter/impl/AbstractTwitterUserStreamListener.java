package br.com.porcelli.hornetq.integration.twitter.impl;

import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public abstract class AbstractTwitterUserStreamListener implements
		UserStreamListener {

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2) {
	}

	@Override
	public void onFollow(User arg0, User arg1) {
	}

	@Override
	public void onFriendList(int[] arg0) {
	}

	@Override
	public void onBlock(User arg0, User arg1) {
	}

	@Override
	public void onUnblock(User arg0, User arg1) {
	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2) {
	}

	@Override
	public void onUnfollow(User arg0, User arg1) {
	}

	@Override
	public void onUserListCreated(User arg0, UserList arg1) {
	}

	@Override
	public void onUserListDestroyed(User arg0, UserList arg1) {
	}

	@Override
	public void onUserListSubscribed(User arg0, User arg1, UserList arg2) {
	}

	@Override
	public void onUserListUpdated(User arg0, UserList arg1) {
	}

}
