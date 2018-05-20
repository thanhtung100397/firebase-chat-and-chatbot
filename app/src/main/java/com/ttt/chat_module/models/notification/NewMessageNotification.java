package com.ttt.chat_module.models.notification;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.BaseMessage;

public abstract class NewMessageNotification {
    public static final String ROOM_ID = "roomID";
    public static final String TO_USER_ID = "toUserID";
    public static final String OWNER_NAME = "ownerName";
    public static final String OWNER_AVATAR_URL = "ownerAvatarUrl";
    public static final String MESSAGE_TYPE = "messageType";

    private String roomID;
    private String toUserID;
    private String ownerName;
    private String ownerAvatarUrl;
    private String messageType;

    public NewMessageNotification(String roomID, String toUserID, UserInfo ownerInfo, BaseMessage message) {
        this.roomID = roomID;
        this.toUserID = toUserID;
        this.ownerName = ownerInfo.getLastName() + " " + ownerInfo.getFirstName();
        this.ownerAvatarUrl = ownerInfo.getAvatarUrl();
        this.messageType = message.getType();
    }

    public NewMessageNotification() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    public void setOwnerAvatarUrl(String ownerAvatarUrl) {
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
