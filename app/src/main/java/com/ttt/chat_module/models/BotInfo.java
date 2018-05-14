package com.ttt.chat_module.models;

public class BotInfo {
    private String avatarUrl;

    public BotInfo() {
    }

    public BotInfo(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
