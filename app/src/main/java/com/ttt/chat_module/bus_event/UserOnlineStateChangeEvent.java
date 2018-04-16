package com.ttt.chat_module.bus_event;

public class UserOnlineStateChangeEvent {
    private String userID;
    private boolean isOnline;

    public UserOnlineStateChangeEvent(String userID, boolean isOnline) {
        this.userID = userID;
        this.isOnline = isOnline;
    }

    public UserOnlineStateChangeEvent() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
