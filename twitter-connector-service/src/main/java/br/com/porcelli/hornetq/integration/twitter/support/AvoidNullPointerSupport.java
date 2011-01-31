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

import java.net.URL;
import java.util.Date;

import twitter4j.HashtagEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

public final class AvoidNullPointerSupport {

    private AvoidNullPointerSupport() {}

    public static String read(Boolean value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(Date value) {
        if (value != null) { return value.toString(); }
        return "";
    }

    public static String read(URL value) {
        if (value != null) { return value.toString(); }
        return "";
    }

    public static String read(Double value) {
        if (value != null) { return value.toString(); }
        return "";
    }

    public static String read(Integer value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(Long value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(String value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(UserMentionEntity[] value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(HashtagEntity[] value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(URLEntity[] value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

    public static String read(String[] value) {
        if (value != null) { return String.valueOf(value); }
        return "";
    }

}
