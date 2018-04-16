package com.ttt.chat_module.bus_event;

import java.util.Map;

public class AllImageItemsUploadCompleteEvent {
    private String roomID;
    private Map<String, String> imageUrls;

    public AllImageItemsUploadCompleteEvent(String roomID, Map<String, String> imageUrls) {
        this.roomID = roomID;
        this.imageUrls = imageUrls;
    }

    public AllImageItemsUploadCompleteEvent() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Map<String, String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Map<String, String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
