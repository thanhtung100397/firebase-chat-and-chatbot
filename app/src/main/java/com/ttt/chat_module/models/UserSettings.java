package com.ttt.chat_module.models;

public class UserSettings {
    private boolean enableNotification;

    public UserSettings(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public UserSettings() {
        enableNotification = true;
    }

    public boolean getEnableNotification() {
        return enableNotification;
    }

    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }
}
