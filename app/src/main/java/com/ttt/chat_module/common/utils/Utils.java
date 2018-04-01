package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cn.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }
}
