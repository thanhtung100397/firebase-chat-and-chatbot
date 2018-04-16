package com.ttt.chat_module.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.notification.TopicNotification;
import com.ttt.chat_module.services.google_api.GoogleApiClient;
import com.ttt.chat_module.services.google_api.firebase_topic_notification.FirebaseTopicNotificationService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendNotificationService extends Service {
    private static final String TAG = "SendNotificationService";
    private IBinder binder = new SendNotificationBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class SendNotificationBinder extends Binder {
        public SendNotificationService getService() {
            return SendNotificationService.this;
        }
    }

    @SuppressLint("CheckResult")
    public <T> void sendTopicNotification(TopicNotification<T> topicNotification) {
        GoogleApiClient.getClient(Constants.FIREBASE_CLOUD_MESSAGING_API_URL, this)
                .create(FirebaseTopicNotificationService.class)
                .sendTopicNotification(topicNotification)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i(TAG, "sendTopicNotification: success");
                }, error -> {
                    Log.e(TAG, "sendTopicNotification: " + error.getMessage());
                });
    }
}
