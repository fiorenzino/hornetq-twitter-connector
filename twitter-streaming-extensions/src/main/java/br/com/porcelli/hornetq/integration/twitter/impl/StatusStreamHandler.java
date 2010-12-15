package br.com.porcelli.hornetq.integration.twitter.impl;

import java.util.Map;

import org.hornetq.core.persistence.StorageManager;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.utils.ConfigurationHelper;

import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import br.com.porcelli.hornetq.integration.twitter.InternalTwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.listener.AbstractStatusBaseStreamListener;

public class StatusStreamHandler extends
		BaseStreamHandler<AbstractStatusBaseStreamListener> {

	private final String[] mentionedUsers;
	private final String[] hashTags;

	public StatusStreamHandler(final String connectorName,
			final Map<String, Object> configuration,
			final StorageManager storageManager, final PostOffice postOffice) {
		super(connectorName, configuration, storageManager, postOffice);

		this.mentionedUsers = splitProperty(ConfigurationHelper
				.getStringProperty(
						InternalTwitterConstants.PROP_MENTIONED_USERS, null,
						configuration));

		this.hashTags = splitProperty(ConfigurationHelper.getStringProperty(
				InternalTwitterConstants.PROP_HASHTAGS, null, configuration));

	}

	protected void startStreaming() throws TwitterException {
		this.twitterStream = new TwitterStreamFactory(conf).getInstance();
		for (Class<? extends AbstractStatusBaseStreamListener> activeListener : listeners) {
			AbstractStatusBaseStreamListener newListener = buildListenerInstance((Class<AbstractStatusBaseStreamListener>) activeListener);
			if (newListener != null) {
				this.twitterStream.addListener(newListener);
			}
		}

		if (mentionedUsers != null || hashTags != null) {
			FilterQuery fq = new FilterQuery();
			if (mentionedUsers != null) {
				Twitter twitter = new TwitterFactory(conf).getInstance();
				int[] userIds = null;
				try {
					userIds = userIds(twitter.lookupUsers(mentionedUsers));
				} catch (TwitterException e) {
				}
				if (userIds != null) {
					fq.follow(userIds);
				}
			}
			if (hashTags != null) {
				fq.track(hashTags);
			}
			twitterStream.filter(fq);
		} else {
			twitterStream.firehose(1000);
		}
	}

	private int[] userIds(ResponseList<User> users) {
		if (users == null || users.size() == 0) {
			return new int[0];
		}
		int[] ids = new int[users.size()];
		for (int i = 0; i < users.size(); i++) {
			ids[i] = users.get(i).getId();
		}
		return ids;
	}
}