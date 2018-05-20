package com.ttt.chat_module.models.notification;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.TextMessage;

public class NewTextMessageNotification extends NewMessageNotification {
    public static final String MESSAGE = "message";

    private String message;

    public NewTextMessageNotification(String roomID, String toUserID, UserInfo ownerInfo, TextMessage message) {
        super(roomID, toUserID, ownerInfo, message);
        this.message = message.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
