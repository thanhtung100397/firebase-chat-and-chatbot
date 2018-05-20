package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentInteractor extends BaseInteractor {
    void sendTextMessage(String roomID, String message, OnSendMessageCompleteListener listener);

    void sendEmojiImageMessage(String roomID, String type, String emojiID, OnSendMessageCompleteListener listener);

    void sendLocationMessage(String roomID, Location location, String address, OnSendMessageCompleteListener listener);

    void registerUserVisitStateListener(String roomID, OnUserVisitStateChangeListener listener);
    void unregisterUserVisitStateListener();

    void registerOnMessageChangedListener(String roomID, int pageSize, OnMessageChangedListener listener);
    void unregisterOnMessageChangedListener();

    void registerUserStateChangeListener(Map<String, UserInfo> usersInfoMap, OnUserChangeListener listener);
    void unRegisterUserStateChangeListener();

    void registerFriendTypingListener(String roomID, String ignoreUserID, OnTypingStateChangeListener listener);
    void unregisterFriendTypingListener();

    void registerUserSettingsChangeListener(String roomID, OnUserSettingsChangeListener listener);
    void unregisterUserSettingChangeListener();

    void getMessages(String roomID, BaseMessage lastMessage, int pageSize,
                     OnGetMessagesCompleteListener listener);

    void updateUserTypingState(String roomID, String userID, boolean state, OnRequestCompleteListener listener);

    void enterRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener);

    void leftRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener);

    void updateUserSettings(String roomID, String userID, boolean enableNotification, OnRequestCompleteListener listener);
}
