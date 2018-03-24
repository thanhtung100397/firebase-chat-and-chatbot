package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ttt.chat_module.common.Constants;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class UserAuth {
    public static final String USER_SHARE_PREFERENCES = "user_prefs";

    public static String getUserID() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            return firebaseUser.getEmail();
        }
        return null;
    }

    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void saveLoginState(Context context, String userID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_EMAIL, userID);
        editor.apply();
    }

    public static void saveLogoutState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_EMAIL, null);
        editor.apply();
    }
}
