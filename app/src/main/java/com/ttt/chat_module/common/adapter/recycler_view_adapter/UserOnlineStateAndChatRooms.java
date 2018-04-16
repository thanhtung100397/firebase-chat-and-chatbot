package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import com.ttt.chat_module.models.ChatRoom;

import java.util.ArrayList;
import java.util.List;

public class UserOnlineStateAndChatRooms {
    private String userID;
    private boolean isOnline;
    private List<ChatRoom> chatRooms;

    public UserOnlineStateAndChatRooms(String userID) {
        this.userID = userID;
        this.chatRooms = new ArrayList<>(1);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        for (ChatRoom chatRoom : chatRooms){
            chatRoom.updateOnlineState(userID, isOnline);
        }
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void addChatRoom(ChatRoom chatRoom) {
        chatRoom.updateOnlineState(userID, isOnline);
        chatRooms.add(chatRoom);

    }
}
