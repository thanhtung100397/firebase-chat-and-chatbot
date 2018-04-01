package com.ttt.chat_module.models.message_models;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class TextMessage extends BaseMessage {
    private String message;

    public TextMessage(String ownerID, String message) {
        super(ownerID, TEXT_MESSAGE);
        this.message = message;
    }

    public TextMessage() {
    }

    public void update(TextMessage textMessage) {
        setOwnerID(textMessage.getOwnerID());
        setMessage(textMessage.getMessage());
        setCreatedDate(textMessage.getCreatedDate());
        setSeenBy(textMessage.getSeenBy());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
