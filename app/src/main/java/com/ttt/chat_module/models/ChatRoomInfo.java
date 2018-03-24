package com.ttt.chat_module.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatRoomInfo {
    public static final String USER_IDS = "userIDs";

    private Map<String, Boolean> userIDs;

    public ChatRoomInfo(String[] userIDs) {
        this.userIDs = new HashMap<>(userIDs.length);
        for (String userID : userIDs) {
            this.userIDs.put(userID, true);
        }
    }

    public Map<String, Boolean> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(Map<String, Boolean> userIDs) {
        this.userIDs = userIDs;
    }
}
