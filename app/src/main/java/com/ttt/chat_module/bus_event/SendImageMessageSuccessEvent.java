package com.ttt.chat_module.bus_event;

public class SendImageMessageSuccessEvent {
    private String roomID;

    public SendImageMessageSuccessEvent(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
