package com.ttt.chat_module.bus_event;

import com.ttt.chat_module.models.message_models.ImageMessage;

public class SendImageMessageFailureEvent {
    private String roomID;
    private ImageMessage imageMessage;

    public SendImageMessageFailureEvent(String roomID, ImageMessage imageMessage) {
        this.roomID = roomID;
        this.imageMessage = imageMessage;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public ImageMessage getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(ImageMessage imageMessage) {
        this.imageMessage = imageMessage;
    }
}
