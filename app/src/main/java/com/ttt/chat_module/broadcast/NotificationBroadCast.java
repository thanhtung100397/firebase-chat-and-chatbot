package com.ttt.chat_module.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserSettings;
import com.ttt.chat_module.services.firebase.AppFirebaseMessagingService;

public class NotificationBroadCast extends BroadcastReceiver{
    public static final String ACTION_TURN_OFF_NOTIFICATION = "ACTION_TURN_OFF_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == null) {
            return;
        }
        switch (action) {
            case ACTION_TURN_OFF_NOTIFICATION: {
                String roomID = intent.getStringExtra(Constants.KEY_ROOM_ID);
                String userID = intent.getStringExtra(Constants.KEY_USER_ID);
                if(roomID != null) {
                    FirebaseFirestore.getInstance()
                            .collection(Constants.CHAT_ROOMS_COLLECTION)
                            .document(roomID)
                            .collection(ChatRoomInfo.USER_SETTINGS)
                            .document(userID)
                            .set(new UserSettings(false), SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                if(notificationManager != null) {
                                    notificationManager.cancel(roomID, AppFirebaseMessagingService.NOTIFICATION_ID);
                                }
                                ToastUtils.quickToast(context, R.string.notification_turned_off);
                            })
                            .addOnFailureListener(e -> {
                                ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                            });
                }
            }
            break;

            default:{
                break;
            }
        }
    }
}
