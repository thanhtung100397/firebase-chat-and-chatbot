package com.ttt.chat_module.models;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomUsersInfo {
    private Map<String, UserInfo> usersInfo;

    public ChatRoomUsersInfo(String userID, UserInfo userInfo) {
        this.usersInfo = new HashMap<>(1);
        this.usersInfo.put(userID, userInfo);
    }

    public ChatRoomUsersInfo() {
    }

    public Map<String, UserInfo> getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(Map<String, UserInfo> usersInfo) {
        this.usersInfo = usersInfo;
    }
}
