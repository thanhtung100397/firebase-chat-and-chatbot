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
    public static final String VISIT_STATES = "visitStates";
    public static final String TYPING_STATES = "typingStates";
    public static final String LAST_MESSAGE = "lastMessage";
    public static final String MESSAGES = "messages";

    private String id;
    private Map<String, UserInfo> usersInfo;

    public ChatRoomInfo() {
    }

    public ChatRoomInfo(String id, List<UserInfo> usersInfo) {
        this.id = id;
        this.usersInfo = new HashMap<>(usersInfo.size());
        for (UserInfo userInfo : usersInfo) {
            this.usersInfo.put(userInfo.getId(), userInfo);
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
