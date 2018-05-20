package com.ttt.chat_module.bus_event;

import com.ttt.chat_module.models.ChatRoom;

public class ChatRoomChangeEvent {
    private ChatRoom chatRoom;

    public ChatRoomChangeEvent(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatRoomChangeEvent() {
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
