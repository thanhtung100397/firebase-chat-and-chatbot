package com.ttt.chat_module.models;

public class TypingState {
    public static final String IS_TYPING = "isTyping";

    private boolean isTyping;

    public boolean getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }
}
