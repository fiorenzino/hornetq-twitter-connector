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
package br.com.porcelli.hornetq.integration.twitter.support;

import static br.com.porcelli.hornetq.integration.twitter.support.AvoidNullPointerSupport.read;

import org.hornetq.api.core.SimpleString;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.core.server.impl.ServerMessageImpl;

import twitter4j.DirectMessage;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.User;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants;
import br.com.porcelli.hornetq.integration.twitter.TwitterConstants.MessageType;
import br.com.porcelli.hornetq.integration.twitter.data.InternalTwitterConstants;

public final class TweetMessageConverterSupport {

    private TweetMessageConverterSupport() {}

    public static ServerMessage buildMessage(final String queueName, final Status status) {
        final ServerMessage msg = new ServerMessageImpl(status.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE, MessageType.TWEET.toString());
        msg.putStringProperty(TwitterConstants.KEY_CREATED_AT, read(status.getCreatedAt()));
        msg.putStringProperty(TwitterConstants.KEY_ID, read(status.getId()));

        msg.putStringProperty(TwitterConstants.KEY_TEXT, read(status.getText()));
        msg.putStringProperty(TwitterConstants.KEY_SOURCE, read(status.getSource()));
        msg.putStringProperty(TwitterConstants.KEY_TRUNCATED, read(status.isTruncated()));
        msg.putStringProperty(TwitterConstants.KEY_IN_REPLY_TO_STATUS_ID, read(status.getInReplyToStatusId()));
        msg.putStringProperty(TwitterConstants.KEY_IN_REPLY_TO_USER_ID, read(status.getInReplyToUserId()));
        msg.putStringProperty(TwitterConstants.KEY_IN_REPLY_TO_SCREEN_NAME, read(status.getInReplyToScreenName()));

        msg.putStringProperty(TwitterConstants.KEY_RETWEET, read(status.isRetweet()));
        msg.putStringProperty(TwitterConstants.KEY_FAVORITED, read(status.isFavorited()));

        msg.putStringProperty(TwitterConstants.KEY_ENTITIES_URLS_JSON, read(status.getURLEntities()));
        msg.putStringProperty(TwitterConstants.KEY_ENTITIES_HASHTAGS_JSON, read(status.getHashtagEntities()));
        msg.putStringProperty(TwitterConstants.KEY_ENTITIES_MENTIONS_JSON, read(status.getUserMentionEntities()));

        msg.putStringProperty(TwitterConstants.KEY_CONTRIBUTORS_JSON, read(status.getContributors()));

        if (status.getUser() != null) {
            buildUserData("", status.getUser(), msg);
        }

        GeoLocation gl;
        if ((gl = status.getGeoLocation()) != null) {
            msg.putStringProperty(TwitterConstants.KEY_GEO_LATITUDE, read(gl.getLatitude()));
            msg.putStringProperty(TwitterConstants.KEY_GEO_LONGITUDE, read(gl.getLongitude()));
        }

        Place place;
        if ((place = status.getPlace()) != null) {
            msg.putStringProperty(TwitterConstants.KEY_PLACE_ID, read(place.getId()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_URL, read(place.getURL()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_NAME, read(place.getName()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_FULL_NAME, read(place.getFullName()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_COUNTRY_CODE, read(place.getCountryCode()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_COUNTRY, read(place.getCountry()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_STREET_ADDRESS, read(place.getStreetAddress()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_TYPE, read(place.getPlaceType()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_GEO_TYPE, read(place.getGeometryType()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_BOUNDING_BOX_TYPE, read(place.getBoundingBoxType()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_BOUNDING_BOX_COORDINATES_JSON, read(place
                .getBoundingBoxCoordinates().toString()));
            msg.putStringProperty(TwitterConstants.KEY_PLACE_BOUNDING_BOX_GEOMETRY_COORDINATES_JSON, read(place
                .getGeometryCoordinates().toString()));
        }

        msg.putStringProperty(TwitterConstants.KEY_RAW_JSON, status.toString());

        return msg;
    }

    public static ServerMessage buildMessage(final String queueName, final DirectMessage dm) {

        final ServerMessage msg = new ServerMessageImpl(dm.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE, MessageType.DM.toString());

        msg.putStringProperty(TwitterConstants.KEY_ID, read(dm.getId()));
        msg.putStringProperty(TwitterConstants.KEY_TEXT, read(dm.getText()));
        msg.putStringProperty(TwitterConstants.KEY_CREATED_AT, read(dm.getCreatedAt()));

        if (dm.getSender() != null) {
            buildUserData(InternalTwitterConstants.KEY_USER_SENDER_PREFIX, dm.getSender(), msg);
        }

        if (dm.getRecipient() != null) {
            buildUserData(InternalTwitterConstants.KEY_USER_RECIPIENT_PREFIX, dm.getRecipient(), msg);
        }

        msg.putStringProperty(TwitterConstants.KEY_RAW_JSON, dm.toString());

        return msg;
    }

    public static ServerMessage buildMessage(final String queueName, final Tweet tweet) {
        final ServerMessage msg = new ServerMessageImpl(tweet.getId(), InternalTwitterConstants.INITIAL_MESSAGE_BUFFER_SIZE);
        msg.setAddress(new SimpleString(queueName));
        msg.setDurable(true);

        msg.putStringProperty(TwitterConstants.KEY_MSG_TYPE, MessageType.TWEET.toString());

        msg.putStringProperty(TwitterConstants.KEY_ID, read(tweet.getId()));
        msg.putStringProperty(TwitterConstants.KEY_TEXT, read(tweet.getText()));
        msg.putStringProperty(TwitterConstants.KEY_SOURCE, read(tweet.getSource()));

        msg.putStringProperty(TwitterConstants.KEY_FROM_USER_ID, read(tweet.getFromUserId()));
        msg.putStringProperty(TwitterConstants.KEY_FROM_USER_NAME, read(tweet.getFromUser()));
        msg.putStringProperty(TwitterConstants.KEY_FROM_USER_PROFILE_IMAGE_URL, read(tweet.getProfileImageUrl()));

        msg.putStringProperty(TwitterConstants.KEY_TO_USER_ID, read(tweet.getToUserId()));
        msg.putStringProperty(TwitterConstants.KEY_TO_USER_SCREEN_NAME, read(tweet.getToUser()));
        msg.putStringProperty(TwitterConstants.KEY_LOCATION, read(tweet.getLocation()));
        msg.putStringProperty(TwitterConstants.KEY_ISO_LANG_CODE, read(tweet.getIsoLanguageCode()));

        msg.putStringProperty(TwitterConstants.KEY_CREATED_AT, read(tweet.getCreatedAt()));

        GeoLocation gl;
        if ((gl = tweet.getGeoLocation()) != null) {
            msg.putStringProperty(TwitterConstants.KEY_GEO_LATITUDE, read(gl.getLatitude()));
            msg.putStringProperty(TwitterConstants.KEY_GEO_LONGITUDE, read(gl.getLongitude()));
        }

        msg.putStringProperty(TwitterConstants.KEY_RAW_JSON, tweet.toString());

        return msg;
    }

    private static void buildUserData(final String prefix, final User user, ServerMessage msg) {
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_ID, read(user.getId()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_NAME, read(user.getName()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_SCREEN_NAME, read(user.getScreenName()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_LOCATION, read(user.getLocation()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_DESCRIPTION, read(user.getDescription()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_PROFILE_IMAGE_URL, read(user.getProfileImageURL()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_URL, read(user.getURL()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_LANG, read(user.getLang()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_PROTECTED, read(user.isProtected()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_FOLLOWERS_COUNT, read(user.getFollowersCount()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_FRIENDS_COUNT, read(user.getFriendsCount()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_CREATED_AT, read(user.getCreatedAt()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_FAVOURITES_COUNT, read(user.getFavouritesCount()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_UTC_OFFSET, read(user.getUtcOffset()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_TIME_ZONE, read(user.getTimeZone()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_STATUSES_COUNT, read(user.getStatusesCount()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_VERIFIED, read(user.isVerified()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_CONTRIBUTORS_ENABLED, read(user.isContributorsEnabled()));
        msg.putStringProperty(prefix + TwitterConstants.KEY_USER_GEO_ENABLED, read(user.isGeoEnabled()));
    }

}
