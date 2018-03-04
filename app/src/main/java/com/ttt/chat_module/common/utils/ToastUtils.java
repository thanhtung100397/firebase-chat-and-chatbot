package com.ttt.chat_module.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public class ToastUtils {
    public static void quickToast(Context context, int messageID) {
        Toast.makeText(context, messageID, Toast.LENGTH_SHORT).show();
    }

    public static void quickToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
