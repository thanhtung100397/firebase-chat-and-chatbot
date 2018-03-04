package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ttt.chat_module.common.Constants;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class UserAuth {
    public static final String USER_SHARE_PREFERENCES = "user_prefs";

    public static String getUserID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
    }

    public static void saveUserID(Context context, String userID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_EMAIL, userID);
        editor.apply();
    }
}
