package com.ttt.chat_module.models.message_models;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class TextMessage extends BaseMessage {
    public static final String MESSAGE = "message";

    private String message;

    public TextMessage(String ownerID, String message) {
        super(ownerID, TEXT_MESSAGE);
        this.message = message;
    }

    public TextMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
