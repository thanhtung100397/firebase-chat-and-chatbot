package com.ttt.chat_module.services.google_api.firebase_topic_notification;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.notification.TopicNotification;
import com.ttt.chat_module.models.notification.TopicNotificationResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseTopicNotificationService {
    @POST("fcm/send")
    @Headers({
            "Authorization: key=" + Constants.FIREBASE_LEGACY_SERVER_KEY
    })
    Observable<TopicNotificationResponse> sendTopicNotification(@Body TopicNotification topicNotification);
}
