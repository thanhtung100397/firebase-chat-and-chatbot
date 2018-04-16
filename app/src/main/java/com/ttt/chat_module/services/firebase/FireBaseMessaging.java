package com.ttt.chat_module.services.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.RequestManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.notification.NewImageMessageNotification;
import com.ttt.chat_module.models.notification.NewMessageNotification;
import com.ttt.chat_module.models.notification.NewTextMessageNotification;
import com.ttt.chat_module.views.chat.activity.ChatActivity;

import java.util.Map;

/**
 * Created by TranThanhTung on 12/7/2017.
 */

public class FireBaseMessaging extends FirebaseMessagingService {
    private static final String TAG = FireBaseMessaging.class.getSimpleName();
    private static final int NEW_MESSAGE_NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (UserAuth.isUserLoggedIn()) {
            buildNotification(data);
        }
    }

    public void buildNotification(Map<String, String> data) {
        try {
            String messageType = data.get(NewMessageNotification.MESSAGE_TYPE);
            if (messageType == null) {
                return;
            }
            String contentText;
            switch (messageType) {
                case BaseMessage.TEXT_MESSAGE: {
                    contentText = data.get(NewTextMessageNotification.MESSAGE);
                }
                break;

                case BaseMessage.IMAGE_MESSAGE: {
                    int imageCount = Integer.parseInt(data.get(NewImageMessageNotification.IMAGE_COUNT));
                    if (imageCount > 1) {
                        contentText = getString(R.string.Has_sent) + " " + imageCount + " " + getString(R.string.images);
                    } else {
                        contentText = getString(R.string.Has_sent) + " " + getString(R.string.an_image);
                    }
                }
                break;

                default: {
                    contentText = "UNKNOWN MESSAGE TYPE";
                    break;
                }
            }
            String roomID = data.get(NewMessageNotification.ROOM_ID);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(Constants.KEY_ROOM_ID, roomID);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    NEW_MESSAGE_NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(data.get(NewMessageNotification.OWNER_NAME))
                            .setContentText(contentText)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(contentText))
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true);

            Log.i(TAG, "buildNotification: "+data.get(NewMessageNotification.OWNER_AVATAR_URL));

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(roomID, NEW_MESSAGE_NOTIFICATION_ID, mBuilder.build());
        } catch (Exception e) {
            Log.e(TAG, "buildNotification: " + e.getMessage());
        }
    }
}
