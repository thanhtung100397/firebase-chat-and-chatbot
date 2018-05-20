package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import com.ttt.chat_module.models.UserInfo;

import java.util.HashSet;
import java.util.Set;

public class UserInfoChatRooms {
    private UserInfo userInfo;
    private Set<String> roomIDs;

    public UserInfoChatRooms(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.roomIDs = new HashSet<>(1);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Set<String> getRoomIDs() {
        return roomIDs;
    }

    public void setRoomIDs(Set<String> roomIDs) {
        this.roomIDs = roomIDs;
    }

    public void addRoomID(String roomID) {
        this.roomIDs.add(roomID);
    }
}
