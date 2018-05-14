package com.ttt.chat_module.models.chat_bot_message;

public class SimpleMessage {
    private String message;

    public SimpleMessage(String message) {
        this.message = message;
    }

    public SimpleMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
