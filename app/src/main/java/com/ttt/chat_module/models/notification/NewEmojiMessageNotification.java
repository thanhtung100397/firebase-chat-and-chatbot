package com.ttt.chat_module.models.notification;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.EmojiMessage;

public class NewEmojiMessageNotification extends NewMessageNotification {
    public static final String MESSAGE = "message";

    private String emojiGroup;
    private String emojiID;

    public NewEmojiMessageNotification(String roomID, UserInfo userInfo, EmojiMessage message) {
        super(roomID, userInfo, message);
        this.emojiGroup = message.getEmojiGroup();
        this.emojiID = message.getEmojiID();
    }

    public String getEmojiGroup() {
        return emojiGroup;
    }

    public void setEmojiGroup(String emojiGroup) {
        this.emojiGroup = emojiGroup;
    }

    public String getEmojiID() {
        return emojiID;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }
}
