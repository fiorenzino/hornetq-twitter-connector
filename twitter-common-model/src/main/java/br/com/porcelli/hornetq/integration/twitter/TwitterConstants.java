package br.com.porcelli.hornetq.integration.twitter;

public interface TwitterConstants {
    public static final String KEY_MSG_TYPE                                     = "msg_type";
    public static final String KEY_RAW_JSON                                     = "twitter.json";

    public static final String KEY_ID                                           = "id";
    public static final String KEY_TEXT                                         = "text";
    public static final String KEY_SOURCE                                       = "source";
    public static final String KEY_CREATED_AT                                   = "created_at";
    public static final String KEY_TRUNCATED                                    = "truncated";
    public static final String KEY_IN_REPLY_TO_STATUS_ID                        = "in_reply_to_status_id";
    public static final String KEY_IN_REPLY_TO_USER_ID                          = "in_reply_to_user_id";
    public static final String KEY_IN_REPLY_TO_SCREEN_NAME                      = "in_reply_to_screen_name";
    public static final String KEY_FAVORITED                                    = "favorited";
    public static final String KEY_RETWEET                                      = "retweet";

    public static final String KEY_FROM_USER_ID                                 = "from_user_id";
    public static final String KEY_FROM_USER_NAME                               = "from_user";
    public static final String KEY_FROM_USER_PROFILE_IMAGE_URL                  = "profile_image_url";
    public static final String KEY_TO_USER_ID                                   = "to_user_id";
    public static final String KEY_TO_USER_NAME                                 = "to_user";
    public static final String KEY_LOCATION                                     = "location";
    public static final String KEY_ISO_LANG_CODE                                = "iso_language_code";

    public static final String KEY_GEO_LATITUDE                                 = "geo.latitude";
    public static final String KEY_GEO_LONGITUDE                                = "geo.longitude";

    public static final String KEY_ENTITIES_URLS_JSON                           = "entities.urls.json";
    public static final String KEY_ENTITIES_HASHTAGS_JSON                       = "entities.hashtags.json";
    public static final String KEY_ENTITIES_MENTIONS_JSON                       = "entities.user_mentions.json";
    public static final String KEY_CONTRIBUTORS_JSON                            = "contributors.json";

    public static final String KEY_PLACE_ID                                     = "place.id";
    public static final String KEY_PLACE_NAME                                   = "place.name";
    public static final String KEY_PLACE_FULL_NAME                              = "place.full_name";
    public static final String KEY_PLACE_COUNTRY                                = "place.country";
    public static final String KEY_PLACE_COUNTRY_CODE                           = "place.country_code";
    public static final String KEY_PLACE_URL                                    = "place.url";
    public static final String KEY_PLACE_STREET_ADDRESS                         = "place.street_address";
    public static final String KEY_PLACE_TYPE                                   = "place.place_type";
    public static final String KEY_PLACE_GEO_TYPE                               = "place.geo_type";
    public static final String KEY_PLACE_BOUNDING_BOX_TYPE                      = "place.bounding_box.type";
    public static final String KEY_PLACE_BOUNDING_BOX_COORDINATES_JSON          = "place.bounding_box.coordinates.json";
    public static final String KEY_PLACE_BOUNDING_BOX_GEOMETRY_COORDINATES_JSON = "place.bounding_box.geometry_coordinates.json";

    public static final String KEY_USER_ID                                      = "user.id";
    public static final String KEY_USER_NAME                                    = "user.name";
    public static final String KEY_USER_SCREEN_NAME                             = "user.screen_name";
    public static final String KEY_USER_PROTECTED                               = "user.protected";
    public static final String KEY_USER_GEO_ENABLED                             = "user.geo_enabled";
    public static final String KEY_USER_LOCATION                                = "user.location";
    public static final String KEY_USER_CONTRIBUTORS_ENABLED                    = "user.contributors_enabled";
    public static final String KEY_USER_VERIFIED                                = "user.verified";
    public static final String KEY_USER_STATUSES_COUNT                          = "user.statuses_count";
    public static final String KEY_USER_TIME_ZONE                               = "user.time_zone";
    public static final String KEY_USER_UTC_OFFSET                              = "user.utc_offset";
    public static final String KEY_USER_FAVOURITES_COUNT                        = "user.favourites_count";
    public static final String KEY_USER_CREATED_AT                              = "user.created_at";
    public static final String KEY_USER_FRIENDS_COUNT                           = "user.friends_count";
    public static final String KEY_USER_FOLLOWERS_COUNT                         = "user.followers_count";
    public static final String KEY_USER_LANG                                    = "user.lang";
    public static final String KEY_USER_URL                                     = "user.url";
    public static final String KEY_USER_PROFILE_IMAGE_URL                       = "user.profile_image_url";
    public static final String KEY_USER_DESCRIPTION                             = "user.description";

    public static final String KEY_SENDER_USER_ID                               = "sender.user.id";
    public static final String KEY_SENDER_USER_NAME                             = "sender.user.name";
    public static final String KEY_SENDER_USER_SCREEN_NAME                      = "sender.user.screen_name";
    public static final String KEY_SENDER_USER_PROTECTED                        = "sender.user.protected";
    public static final String KEY_SENDER_USER_GEO_ENABLED                      = "sender.user.geo_enabled";
    public static final String KEY_SENDER_USER_LOCATION                         = "sender.user.location";
    public static final String KEY_SENDER_USER_CONTRIBUTORS_ENABLED             = "sender.user.contributors_enabled";
    public static final String KEY_SENDER_USER_VERIFIED                         = "sender.user.verified";
    public static final String KEY_SENDER_USER_STATUSES_COUNT                   = "sender.user.statuses_count";
    public static final String KEY_SENDER_USER_TIME_ZONE                        = "sender.user.time_zone";
    public static final String KEY_SENDER_USER_UTC_OFFSET                       = "sender.user.utc_offset";
    public static final String KEY_SENDER_USER_FAVOURITES_COUNT                 = "sender.user.favourites_count";
    public static final String KEY_SENDER_USER_CREATED_AT                       = "sender.user.created_at";
    public static final String KEY_SENDER_USER_FRIENDS_COUNT                    = "sender.user.friends_count";
    public static final String KEY_SENDER_USER_FOLLOWERS_COUNT                  = "sender.user.followers_count";
    public static final String KEY_SENDER_USER_LANG                             = "sender.user.lang";
    public static final String KEY_SENDER_USER_URL                              = "sender.user.url";
    public static final String KEY_SENDER_USER_PROFILE_IMAGE_URL                = "sender.user.profile_image_url";
    public static final String KEY_SENDER_USER_DESCRIPTION                      = "sender.user.description";

    public static final String KEY_RECIPIENT_USER_ID                            = "recipient.user.id";
    public static final String KEY_RECIPIENT_USER_NAME                          = "recipient.user.name";
    public static final String KEY_RECIPIENT_USER_SCREEN_NAME                   = "recipient.user.screen_name";
    public static final String KEY_RECIPIENT_USER_PROTECTED                     = "recipient.user.protected";
    public static final String KEY_RECIPIENT_USER_GEO_ENABLED                   = "recipient.user.geo_enabled";
    public static final String KEY_RECIPIENT_USER_LOCATION                      = "recipient.user.location";
    public static final String KEY_RECIPIENT_USER_CONTRIBUTORS_ENABLED          = "recipient.user.contributors_enabled";
    public static final String KEY_RECIPIENT_USER_VERIFIED                      = "recipient.user.verified";
    public static final String KEY_RECIPIENT_USER_STATUSES_COUNT                = "recipient.user.statuses_count";
    public static final String KEY_RECIPIENT_USER_TIME_ZONE                     = "recipient.user.time_zone";
    public static final String KEY_RECIPIENT_USER_UTC_OFFSET                    = "recipient.user.utc_offset";
    public static final String KEY_RECIPIENT_USER_FAVOURITES_COUNT              = "recipient.user.favourites_count";
    public static final String KEY_RECIPIENT_USER_CREATED_AT                    = "recipient.user.created_at";
    public static final String KEY_RECIPIENT_USER_FRIENDS_COUNT                 = "recipient.user.friends_count";
    public static final String KEY_RECIPIENT_USER_FOLLOWERS_COUNT               = "recipient.user.followers_count";
    public static final String KEY_RECIPIENT_USER_LANG                          = "recipient.user.lang";
    public static final String KEY_RECIPIENT_USER_URL                           = "recipient.user.url";
    public static final String KEY_RECIPIENT_USER_PROFILE_IMAGE_URL             = "recipient.user.profile_image_url";
    public static final String KEY_RECIPIENT_USER_DESCRIPTION                   = "recipient.user.description";

    public enum MessageType {
        DM,
        TWEET
    }
}
