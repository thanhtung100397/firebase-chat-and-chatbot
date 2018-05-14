package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentInteractor extends BaseInteractor {
    void sendTextMessage(String roomID, String message, OnSendMessageCompleteListener listener);

    void sendEmojiImageMessage(String roomID, String type, String emojiID, OnSendMessageCompleteListener listener);

    void sendLocationMessage(String roomID, Location location, String address, OnSendMessageCompleteListener listener);

    void registerUserVisitStateListener(String roomID, OnUserVisitStateChangeListener listener);

    void unregisterUserVisitStateListener();

    void registerOnMessageChangedListener(String roomID,
                                          int pageSize,
                                          OnMessageChangedListener listener);

    void unregisterOnMessageChangedListener();

    void registerFriendTypingListener(String roomID, String ignoreUserID, OnTypingStateChangeListener listener);

    void unregisterFriendTypingListener();

    void getMessages(String roomID, BaseMessage lastMessage, int pageSize,
                     OnGetMessagesCompleteListener listener);

    void updateUserTypingState(String roomID, String userID, boolean state, OnRequestCompleteListener listener);

    void registerUserStateChangeListener(String userID, OnUserOnlineStateChangeListener listener);
    void unRegisterUserStateChangeListener();

    void enterRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener);

    void leftRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener);
}
