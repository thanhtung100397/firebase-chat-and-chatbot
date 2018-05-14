package com.ttt.chat_module.bus_event;

public class EmojiSelectedEvent {
    private String type;
    private String emojiID;

    public EmojiSelectedEvent(String type, String emojiID) {
        this.type = type;
        this.emojiID = emojiID;
    }

    public EmojiSelectedEvent() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmojiID() {
        return emojiID;
    }

    public void setEmojiID(String emojiID) {
        this.emojiID = emojiID;
    }
}
