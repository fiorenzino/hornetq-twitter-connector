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
