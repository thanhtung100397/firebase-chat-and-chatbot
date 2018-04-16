package com.ttt.chat_module.bus_event;

public class ImageItemUploadFailureEvent {
    private String roomID;
    private int position;

    public ImageItemUploadFailureEvent(String roomID, int position) {
        this.roomID = roomID;
        this.position = position;
    }

    public ImageItemUploadFailureEvent() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
