package com.ttt.chat_module.models.notification;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.LocationMessage;

public class NewLocationMessageNotification extends NewMessageNotification {
    private String lat;
    private String lon;
    private String address;

    public NewLocationMessageNotification(String roomID, String toUserID, UserInfo userInfo, LocationMessage message) {
        super(roomID, toUserID, userInfo, message);
        this.lat = message.getLat() + "";
        this.lat = message.getLon() + "";
        this.address = message.getAddress();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
