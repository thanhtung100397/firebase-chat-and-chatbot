package com.ttt.chat_module.services.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ttt.chat_module.GlideApp;
import com.ttt.chat_module.R;
import com.ttt.chat_module.broadcast.NotificationBroadCast;
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

public class AppFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = AppFirebaseMessagingService.class.getSimpleName();
    public static final int NOTIFICATION_ID = 0;
    private static final int DISABLE_NOTIFICATION_ID = 1;

    private NotificationBroadCast notificationBroadCast;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationBroadCast = new NotificationBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationBroadCast.ACTION_TURN_OFF_NOTIFICATION);
        registerReceiver(notificationBroadCast, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationBroadCast != null) {
            unregisterReceiver(notificationBroadCast);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (UserAuth.isUserLoggedIn()) {
            buildNotification(data);
        }
    }

    public void buildNotification(Map<String, String> data) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager == null) {
            Log.i(TAG, "buildNotification: Notification service not available");
            return;
        }
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

                case BaseMessage.EMOJI_MESSAGE: {
                    contentText = getString(R.string.Has_sent) + " " + getString(R.string.an_emoji);
                }
                break;

                case BaseMessage.LOCATION_MESSAGE: {
                    contentText = getString(R.string.Has_sent) + " " + getString(R.string.a_location);
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
                    NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Bitmap largeBitmap = loadBitmap(data.get(NewMessageNotification.OWNER_AVATAR_URL));
            Intent disableNotificationIntent = new Intent(this, NotificationBroadCast.class);
            disableNotificationIntent.setAction(NotificationBroadCast.ACTION_TURN_OFF_NOTIFICATION);
            disableNotificationIntent.putExtra(Constants.KEY_ROOM_ID, roomID);
            disableNotificationIntent.putExtra(Constants.KEY_USER_ID, data.get(NewMessageNotification.TO_USER_ID));
            PendingIntent disableNotificationPendingIntent = PendingIntent.getBroadcast(this, DISABLE_NOTIFICATION_ID,
                    disableNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setLargeIcon(largeBitmap)
                            .setContentTitle(data.get(NewMessageNotification.OWNER_NAME))
                            .setContentText(contentText)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(contentText))
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .addAction(R.drawable.ic_no_remind, getString(R.string.turn_off_notification), disableNotificationPendingIntent)
                            .setAutoCancel(true);

            mNotificationManager.notify(roomID, NOTIFICATION_ID, mBuilder.build());
        } catch (Exception e) {
            Log.e(TAG, "buildNotification: " + e.getMessage());
        }
    }

    private Bitmap loadBitmap(String url) {
        Bitmap bitmap;
        try {
            bitmap = GlideApp.with(AppFirebaseMessagingService.this)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.avatar_placeholder)
                    .submit()
                    .get();
        } catch (Exception e) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_placeholder);
        }
        return bitmap;
    }
}
