package com.ttt.chat_module.bus_event;

public class ImageItemUploadSuccessEvent {
    private String roomID;
    private int position;
    private String url;

    public ImageItemUploadSuccessEvent(String roomID, int position, String url) {
        this.roomID = roomID;
        this.position = position;
        this.url = url;
    }

    public ImageItemUploadSuccessEvent() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
