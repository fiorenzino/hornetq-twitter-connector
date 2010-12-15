package br.com.porcelli.hornetq.integration.twitter;

public interface TwitterConstants {
	public static final String KEY_MSG_TYPE = "msg.type";
	public static final String KEY_ID = "id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_SOURCE = "source";
	public static final String KEY_CREATED_AT = "createdAt";
	public static final String KEY_IN_REPLY_TO_STATUS_ID = "inReplyToStatusId";
	public static final String KEY_IN_REPLY_TO_USER_ID = "inReplyToUserId";
	public static final String KEY_IS_FAVORITED = "isFavorited";
	public static final String KEY_IS_RETWEET = "isRetweet";
	public static final String KEY_GEO_LOCATION_LATITUDE = "geoLocation.latitude";
	public static final String KEY_GEO_LOCATION_LONGITUDE = "geoLocation.longitude";
	public static final String KEY_DISPLAY_COODINATES = "displayCoodinates";
	public static final String KEY_USER_ID = "user.id";
	public static final String KEY_USER_NAME = "user.name";
	public static final String KEY_USER_SCREEN_NAME = "user.screen.name";

	public static final String KEY_PLACE_ID = "place.id";
	public static final String KEY_PLACE_FULL_NAME = "place.name";
	public static final String KEY_PLACE_COUNTRY_CODE = "place.country.code";

	public enum MessageType {
		DM, TWEET
	}
}
