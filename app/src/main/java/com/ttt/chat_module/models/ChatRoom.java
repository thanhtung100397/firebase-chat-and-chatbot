package com.ttt.chat_module.models;

import android.content.Context;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.TextMessage;

import java.util.Map;

public class ChatRoom {
    private String roomID;
    private Map<String, UserInfo> friendsInfo;
    private String lastMessage;
    private int onlineFriendNumber = 0;

    public ChatRoom(Context context, ChatRoomInfo chatRoomInfo, Map<String, Object> lastMessageMap) {
        update(context, chatRoomInfo, lastMessageMap);
    }

    public void update(Context context, ChatRoomInfo chatRoom, Map<String, Object> lastMessageMap) {
        setRoomID(chatRoom.getId());
        setFriendsInfo(chatRoom.getUsersInfo());
        setLastMessage(context, lastMessageMap);
    }

    public void updateOnlineState(String userID, boolean isOnline) {
        UserInfo userInfo = friendsInfo.get(userID);
        if(userInfo == null) {
            return;
        }
        onlineFriendNumber += - (userInfo.getIsOnline()? 1 : 0) + (isOnline? 1 : 0);
        userInfo.setIsOnline(isOnline);
    }

    public void setLastMessage(Context context, Map<String, Object> lastMessageMap) {
        String ownerID = (String) lastMessageMap.get(BaseMessage.OWNER_ID);
        String messageType = (String) lastMessageMap.get(BaseMessage.TYPE);
        String currentUserID = UserAuth.getUserID();
        switch (messageType) {
            case BaseMessage.TEXT_MESSAGE: {
                String sender;
                if (ownerID.equals(currentUserID)) {
                    sender = context.getString(R.string.you);
                } else {
                    UserInfo friendInfo = friendsInfo.get(ownerID);
                    if (friendInfo == null) {
                        return;
                    }
                    sender = friendInfo.getFirstName();
                }
                this.lastMessage = sender + ": " + lastMessageMap.get(TextMessage.MESSAGE);
            }
            break;

            case BaseMessage.IMAGE_MESSAGE: {
                int imageCount = ((Map) lastMessageMap.get(ImageMessage.IMAGES)).size();
                if (ownerID.equals(currentUserID)) {
                    this.lastMessage = context.getString(R.string.you_have_sent);
                } else {
                    UserInfo friendInfo = friendsInfo.get(ownerID);
                    if (friendInfo == null) {
                        return;
                    }
                    this.lastMessage = friendInfo.getFirstName() + " " + context.getString(R.string.has_sent);
                }
                this.lastMessage += " " + ((imageCount == 1) ? context.getString(R.string.an_image) : (imageCount + " " + context.getString(R.string.images)));
            }
            break;

            case BaseMessage.EMOJI_MESSAGE:{
                if (ownerID.equals(currentUserID)) {
                    this.lastMessage = context.getString(R.string.you_have_sent_an_emoji);
                } else {
                    UserInfo friendInfo = friendsInfo.get(ownerID);
                    if (friendInfo == null) {
                        return;
                    }
                    this.lastMessage = friendInfo.getFirstName() + " " + context.getString(R.string.has_sent_an_emoji);
                }
            }
            break;

            case BaseMessage.LOCATION_MESSAGE:{
                if (ownerID.equals(currentUserID)) {
                    this.lastMessage = context.getString(R.string.you_have_sent_a_location);
                } else {
                    UserInfo friendInfo = friendsInfo.get(ownerID);
                    if (friendInfo == null) {
                        return;
                    }
                    this.lastMessage = friendInfo.getFirstName() + " " + context.getString(R.string.has_sent_a_location);
                }
            }
            break;

            default: {
                break;
            }
        }
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Map<String, UserInfo> getFriendsInfo() {
        return friendsInfo;
    }

    public void setFriendsInfo(Map<String, UserInfo> userInfoMap) {
        userInfoMap.remove(UserAuth.getUserID());
        this.friendsInfo = userInfoMap;
        for (Map.Entry<String, UserInfo> userInfoEntry : friendsInfo.entrySet()){
            if(userInfoEntry.getValue().getIsOnline()){
                this.onlineFriendNumber += 1;
            }
        }
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isOnline() {
        return onlineFriendNumber > 0;
    }
}
