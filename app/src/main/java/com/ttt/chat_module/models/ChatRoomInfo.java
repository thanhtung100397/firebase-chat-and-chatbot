package com.ttt.chat_module.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatRoomInfo implements Serializable {
    public static final String USERS_INFO = "usersInfo";
    public static final String TYPING_STATES = "typingStates";

    private String id;
    private Map<String, UserInfo> usersInfo;

    public ChatRoomInfo() {
    }

    public ChatRoomInfo(String id, List<User> users) {
        this.id = id;
        this.usersInfo = new HashMap<>(users.size());
        for (User user : users) {
            usersInfo.put(user.getId(), new UserInfo(user));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, UserInfo> getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(Map<String, UserInfo> usersInfo) {
        this.usersInfo = usersInfo;
    }
}
