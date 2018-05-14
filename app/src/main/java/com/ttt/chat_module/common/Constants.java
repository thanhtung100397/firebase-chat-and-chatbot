package com.ttt.chat_module.common;

/**
 * Created by Admin on 7/7/2017.
 */

public class Constants {
    public static final int PAGE_SIZE = 30;
    public static final int TIME_SPLASH_SCREEN = 2500;
    public static final String EMOJI_ROOT_ASSETS_FOLDER_PATH = "emoji";
    public static final String ABSOLUTE_EMOJI_ASSETS_FOLDER_PATH = "file:///android_asset/" + EMOJI_ROOT_ASSETS_FOLDER_PATH;
    public static final String DD_MM_YYYY_DATE_FORMAT = "dd/MM/yyyy";

    public static final String PHONE_REGEX = "[0-9]{10,11}";

    public static final String FIREBASE_USER_IMAGES_FOLDER_PATH = "user";
    public static final String FIREBASE_AVATAR_FILE_NAME = "avatar";
    public static final String FIREBASE_COVER_FILE_NAME = "cover";

    public static final String API_AI_CLIENT_ACCESS_TOKEN = "a35929c4f3314fa48c72d0e96ea5446c";
    public static final String FIREBASE_LEGACY_SERVER_KEY = "AIzaSyBz4C2n9BQVkbp0ALp9Cy6nrc8bJ-pUTcA";
    public static final String FIREBASE_CLOUD_MESSAGING_API_URL = "https://fcm.googleapis.com/";

    public static final String USER_INFO_SHARE_PREFS = "user_info_prefs";

    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_USERS_INFO = "KEY_USERS_INFO";

    public static final String USERS_COLLECTION = "users";
    public static final String CHAT_ROOMS_COLLECTION = "chat_rooms";

    public static final String UNNAMED_ROAD = "Unnamed Road";
    public static final String VIETNAM = "Vietnam";
    public static final String ADDRESS = "ADDRESS";
    public static final String LOCATION = "LOCATION";
    public static final String KEY = "key";
    public static final String GOOGLE_MAP_API = "https://maps.googleapis.com/";

    public static final String KEY_CHAT_ROOM_INFO = "KEY_CHAT_ROOM_INFO";
    public static final String KEY_OWNER_ID = "KEY_OWNER_ID";
    public static final String KEY_OWNER = "KEY_OWNER";
    public static final String KEY_IMAGE_URIS = "KEY_IMAGE_URIS";
    public static final String KEY_IMAGE_FOLDER = "KEY_IMAGE_FOLDER";
    public static final String KEY_IMAGE_URL = "KEY_IMAGE_URL";
    public static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";
    public static final String KEY_ROOM_ID = "KEY_ROOM_ID";
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_IN_ROOM_STATE = "KEY_IN_ROOM_STATE";
    public static final String KEY_EMOJI_TYPE = "KEY_EMOJI_TYPE";
    public static final String NOT_AVAILABLE_SYMBOL = "_";
    public static final String KEY_USER_PROFILE = "KEY_USER_PROFILE";

    public static final String CHAT_BOT_ACTION_DIAL = "mobile.dial";
    public static final String CHAT_BOT_ACTION_CALL = "mobile.call";
    public static final String CHAT_BOT_ACTION_CONTACT = "mobile.contact";
    public static final String CHAT_BOT_ACTION_VOLUME = "mobile.volume";
    public static final String CHAT_BOT_ACTION_PLAY_MUSIC = "mobile.music";
    public static final String CHAT_BOT_ACTION_OPEN_FLASHLIGHT = "mobile.flashlight";
    public static final String CHAT_BOT_CONTACT_PARAM = "contact";
    public static final String CHAT_BOT_PHONE_NUMBER_PARAM = "phone";
    public static final String CHAT_BOT_VOLUME_PARAM = "volume";
    public static final String CHAT_BOT_ADJUST_PARAM = "adjust";
    public static final String CHAT_BOT_SILENCE_VOLUME = "im lặng";
    public static final String CHAT_BOT_MAX_VOLUME = "tối đa";
    public static final String CHAT_BOT_STATE_PARAM = "state";
    public static final String CHAT_BOT_ON = "bật";
    public static final String CHAT_BOT_OFF = "tắt";
}
