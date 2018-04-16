package com.ttt.chat_module.models.message_models;

public class EmojiImageMessage extends BaseMessage {
    private String emojiGroup;
    private String emojiID;

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
