package com.ttt.chat_module.bus_event;

import java.util.Map;

public class ChatRoomLastMessageChangeEvent {
    private String roomID;
    private Map<String, Object> lastMessage;

    public ChatRoomLastMessageChangeEvent(String roomID, Map<String, Object> lastMessage) {
        this.roomID = roomID;
        this.lastMessage = lastMessage;
    }

    public ChatRoomLastMessageChangeEvent() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Map<String, Object> getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Map<String, Object> lastMessage) {
        this.lastMessage = lastMessage;
    }
}
