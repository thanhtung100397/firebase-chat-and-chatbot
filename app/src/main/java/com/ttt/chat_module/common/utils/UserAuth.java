package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.UserProfile;

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

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        return new User(sharedPreferences);
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

    public static void saveProfile(Context context, UserProfile userProfile) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userProfile.writeToSharePreferences(editor);
        editor.apply();
    }

    public static UserProfile getProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO_SHARE_PREFS, Context.MODE_PRIVATE);
        return new UserProfile(sharedPreferences);
    }

    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
