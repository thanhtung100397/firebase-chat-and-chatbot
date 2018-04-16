package com.ttt.chat_module.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseDeviceGroupApiResponse {
    @SerializedName("notification_key")
    @Expose
    private String notificationKey;

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }
}
