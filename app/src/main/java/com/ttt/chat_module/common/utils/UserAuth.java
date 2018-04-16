package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class UserAuth {

    public static String getUserID() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            return firebaseUser.getUid();
        }
        return null;
    }

    public static String getUserNotificationKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(User.FCM_TOKEN, null);
    }

    public static void saveUserNotificationKey(Context context, String notificationKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(User.FCM_TOKEN, notificationKey);
        editor.apply();
    }

    public static UserInfo getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        return new UserInfo(sharedPreferences);
    }

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(user == null) {
            user = new User();
        }
        user.writeToSharePreferences(editor);
        editor.apply();
    }

    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
