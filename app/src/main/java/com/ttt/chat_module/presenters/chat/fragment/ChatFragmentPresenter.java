package com.ttt.chat_module.presenters.chat.fragment;

import android.net.Uri;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.presenters.BasePresenter;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentPresenter extends BasePresenter {
    boolean isCoupleChatRoom();
    boolean isUserInChatRoom();
    String getRoomID();
    void registerServices();
    void unregisterServices();
    void validateSentTextMessage(String message);
    void validateEmojiImageMessage(String type, String emojiID);
    void validateLocationMessage(Location location, String address);
    void validateSentImageMessage(List<Uri> imageUris);
    UserInfo getOwnerInfo();
    Map<String, UserInfo> getMapFriendsInfo();
    void seenAllUnseenMessage();
    void fetchLatestMessages();
    void loadMoreMessages();
    void changeUserTypingState(boolean state);
    void onSendImageComplete(ImageMessage imageMessage, boolean isSuccess);
    void leftRoom();
    void enterRoom();
    boolean isOnline();
    void changeUserNotificationFlag();
}
