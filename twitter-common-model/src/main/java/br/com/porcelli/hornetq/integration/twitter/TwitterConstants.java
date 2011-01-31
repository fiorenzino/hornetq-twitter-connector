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
package br.com.porcelli.hornetq.integration.twitter;

public interface TwitterConstants {
    public static final String KEY_MSG_TYPE                                     = "msg_type";
    public static final String KEY_RAW_JSON                                     = "twitter_json";

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
    public static final String KEY_TO_USER_SCREEN_NAME                          = "to_user_screen_name";
    public static final String KEY_LOCATION                                     = "location";
    public static final String KEY_ISO_LANG_CODE                                = "iso_language_code";

    public static final String KEY_GEO_LATITUDE                                 = "geo_latitude";
    public static final String KEY_GEO_LONGITUDE                                = "geo_longitude";

    public static final String KEY_ENTITIES_URLS_JSON                           = "entities_urls_json";
    public static final String KEY_ENTITIES_HASHTAGS_JSON                       = "entities_hashtags_json";
    public static final String KEY_ENTITIES_MENTIONS_JSON                       = "entities_user_mentions_json";
    public static final String KEY_CONTRIBUTORS_JSON                            = "contributors_json";

    public static final String KEY_PLACE_ID                                     = "place_id";
    public static final String KEY_PLACE_NAME                                   = "place_name";
    public static final String KEY_PLACE_FULL_NAME                              = "place_full_name";
    public static final String KEY_PLACE_COUNTRY                                = "place_country";
    public static final String KEY_PLACE_COUNTRY_CODE                           = "place_country_code";
    public static final String KEY_PLACE_URL                                    = "place_url";
    public static final String KEY_PLACE_STREET_ADDRESS                         = "place_street_address";
    public static final String KEY_PLACE_TYPE                                   = "place_place_type";
    public static final String KEY_PLACE_GEO_TYPE                               = "place_geo_type";
    public static final String KEY_PLACE_BOUNDING_BOX_TYPE                      = "place_bounding_box_type";
    public static final String KEY_PLACE_BOUNDING_BOX_COORDINATES_JSON          = "place_bounding_box_coordinates_json";
    public static final String KEY_PLACE_BOUNDING_BOX_GEOMETRY_COORDINATES_JSON = "place_bounding_box_geometry_coordinates_json";

    public static final String KEY_USER_ID                                      = "user_id";
    public static final String KEY_USER_NAME                                    = "user_name";
    public static final String KEY_USER_SCREEN_NAME                             = "user_screen_name";
    public static final String KEY_USER_PROTECTED                               = "user_protected";
    public static final String KEY_USER_GEO_ENABLED                             = "user_geo_enabled";
    public static final String KEY_USER_LOCATION                                = "user_location";
    public static final String KEY_USER_CONTRIBUTORS_ENABLED                    = "user_contributors_enabled";
    public static final String KEY_USER_VERIFIED                                = "user_verified";
    public static final String KEY_USER_STATUSES_COUNT                          = "user_statuses_count";
    public static final String KEY_USER_TIME_ZONE                               = "user_time_zone";
    public static final String KEY_USER_UTC_OFFSET                              = "user_utc_offset";
    public static final String KEY_USER_FAVOURITES_COUNT                        = "user_favourites_count";
    public static final String KEY_USER_CREATED_AT                              = "user_created_at";
    public static final String KEY_USER_FRIENDS_COUNT                           = "user_friends_count";
    public static final String KEY_USER_FOLLOWERS_COUNT                         = "user_followers_count";
    public static final String KEY_USER_LANG                                    = "user_lang";
    public static final String KEY_USER_URL                                     = "user_url";
    public static final String KEY_USER_PROFILE_IMAGE_URL                       = "user_profile_image_url";
    public static final String KEY_USER_DESCRIPTION                             = "user_description";

    public static final String KEY_DISPLAY_COODINATES                           = "displayCoodinates";

    public static final String KEY_SENDER_USER_ID                               = "sender_user_id";
    public static final String KEY_SENDER_USER_NAME                             = "sender_user_name";
    public static final String KEY_SENDER_USER_SCREEN_NAME                      = "sender_user_screen_name";
    public static final String KEY_SENDER_USER_PROTECTED                        = "sender_user_protected";
    public static final String KEY_SENDER_USER_GEO_ENABLED                      = "sender_user_geo_enabled";
    public static final String KEY_SENDER_USER_LOCATION                         = "sender_user_location";
    public static final String KEY_SENDER_USER_CONTRIBUTORS_ENABLED             = "sender_user_contributors_enabled";
    public static final String KEY_SENDER_USER_VERIFIED                         = "sender_user_verified";
    public static final String KEY_SENDER_USER_STATUSES_COUNT                   = "sender_user_statuses_count";
    public static final String KEY_SENDER_USER_TIME_ZONE                        = "sender_user_time_zone";
    public static final String KEY_SENDER_USER_UTC_OFFSET                       = "sender_user_utc_offset";
    public static final String KEY_SENDER_USER_FAVOURITES_COUNT                 = "sender_user_favourites_count";
    public static final String KEY_SENDER_USER_CREATED_AT                       = "sender_user_created_at";
    public static final String KEY_SENDER_USER_FRIENDS_COUNT                    = "sender_user_friends_count";
    public static final String KEY_SENDER_USER_FOLLOWERS_COUNT                  = "sender_user_followers_count";
    public static final String KEY_SENDER_USER_LANG                             = "sender_user_lang";
    public static final String KEY_SENDER_USER_URL                              = "sender_user_url";
    public static final String KEY_SENDER_USER_PROFILE_IMAGE_URL                = "sender_user_profile_image_url";
    public static final String KEY_SENDER_USER_DESCRIPTION                      = "sender_user_description";

    public static final String KEY_RECIPIENT_USER_ID                            = "recipient_user_id";
    public static final String KEY_RECIPIENT_USER_NAME                          = "recipient_user_name";
    public static final String KEY_RECIPIENT_USER_SCREEN_NAME                   = "recipient_user_screen_name";
    public static final String KEY_RECIPIENT_USER_PROTECTED                     = "recipient_user_protected";
    public static final String KEY_RECIPIENT_USER_GEO_ENABLED                   = "recipient_user_geo_enabled";
    public static final String KEY_RECIPIENT_USER_LOCATION                      = "recipient_user_location";
    public static final String KEY_RECIPIENT_USER_CONTRIBUTORS_ENABLED          = "recipient_user_contributors_enabled";
    public static final String KEY_RECIPIENT_USER_VERIFIED                      = "recipient_user_verified";
    public static final String KEY_RECIPIENT_USER_STATUSES_COUNT                = "recipient_user_statuses_count";
    public static final String KEY_RECIPIENT_USER_TIME_ZONE                     = "recipient_user_time_zone";
    public static final String KEY_RECIPIENT_USER_UTC_OFFSET                    = "recipient_user_utc_offset";
    public static final String KEY_RECIPIENT_USER_FAVOURITES_COUNT              = "recipient_user_favourites_count";
    public static final String KEY_RECIPIENT_USER_CREATED_AT                    = "recipient_user_created_at";
    public static final String KEY_RECIPIENT_USER_FRIENDS_COUNT                 = "recipient_user_friends_count";
    public static final String KEY_RECIPIENT_USER_FOLLOWERS_COUNT               = "recipient_user_followers_count";
    public static final String KEY_RECIPIENT_USER_LANG                          = "recipient_user_lang";
    public static final String KEY_RECIPIENT_USER_URL                           = "recipient_user_url";
    public static final String KEY_RECIPIENT_USER_PROFILE_IMAGE_URL             = "recipient_user_profile_image_url";
    public static final String KEY_RECIPIENT_USER_DESCRIPTION                   = "recipient_user_description";

    public enum MessageType {
        DM,
        TWEET
    }
}
