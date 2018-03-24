package com.ttt.chat_module.views.chat.fragment;

import com.ttt.chat_module.models.Message;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentView {
    void addOwnerMessage(Message message);
    void addFriendMessage(Message message);
    void onMessageSeen();

    void showFirstLoadingMessagesProgress();
    void hideFirstLoadingMessagesProgress();
    void showFirstLoadingMessagesError();
}
