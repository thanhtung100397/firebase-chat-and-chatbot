package com.ttt.chat_module.models.message_models;

public class EmojiMessage extends BaseMessage {
    private String emojiGroup;
    private String emojiID;

    public EmojiMessage(String ownerID, String emojiGroup, String emojiID) {
        super(ownerID, BaseMessage.EMOJI_MESSAGE);
        this.emojiGroup = emojiGroup;
        this.emojiID = emojiID;
    }

    public EmojiMessage() {
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
