package com.ttt.chat_module.presenters.chat.fragment;

import android.net.Uri;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.google_map.Location;
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
    void bindNotificationService();
    void unbindNotificationService();
    void registerOnFriendVisitStageListener();
    void unregisterOnFriendVisitStageListener();
    void registerOnMessageAddedListener();
    void unregisterOnMessageAddedListener();
    void registerFriendTypingListener(String ignoreUserID);
    void unregisterFriendTypingListener();
    void validateSentTextMessage(String message);
    void validateEmojiImageMessage(String type, String emojiID);
    void validateLocationMessage(Location location, String address);
    void validateSentImageMessage(List<Uri> imageUris);
    UserInfo getOwnerInfo();
    Map<String, UserInfo> getMapFriendsInfo();
    void fetchLatestMessages();
    void loadMoreMessages();
    void changeUserTypingState(boolean state);
    void registerFriendOnlineStateListener(String userID);
    void unRegisterFriendOnlineStateListener();
    void setHasUploadingTask(boolean hasUploadingTask);
    void leftRoom();
    void enterRoom();
}
