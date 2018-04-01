package com.ttt.chat_module.presenters.chat.fragment;

import android.net.Uri;

import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentInteractor extends BaseInteractor {
    void sendTextMessage(String roomID, String message, OnRequestCompleteListener listener);

    void registerOnMessageChangedListener(String roomID,
                                          int pageSize,
                                          OnMessageChangedListener listener);

    void registerFriendTypingListener(String roomID, String ignoreUserID, OnTypingStateChangeListener listener);

    void unregisterFriendTypingListener();

    void unregisterOnMessageChangedListener();

    void getMessages(String roomID, TextMessage lastTextMessage, int pageSize,
                     OnGetMessagesCompleteListener listener);

    void updateUserTypingState(String roomID, String userID, boolean state, OnRequestCompleteListener listener);

    void registerUserStateChangeListener(String userID, OnUserOnlineStateChangeListener listener);
    void unRegisterUserStateChangeListener();
}
