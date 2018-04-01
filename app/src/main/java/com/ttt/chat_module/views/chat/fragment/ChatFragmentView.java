package com.ttt.chat_module.views.chat.fragment;

import com.ttt.chat_module.models.message_models.TextMessage;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentView {
    void addTopMessage(TextMessage textMessage);
    void addMessages(List<TextMessage> textMessages);
    void updateMessageState(TextMessage textMessage, int position);
    void onMessageSeen();

    void showFirstLoadingMessagesProgress();
    void hideFirstLoadingMessagesProgress();
    void showFirstLoadingMessagesError();

    void clearTypedMessage();
    void clearAllMessages();

    void showLoadingMoreProgress();
    void hideLoadingMoreProgress();

    void showTypingStateView(String userID);
    void hideTypingStateView(String userID);

    void updateFriendOnlineState(boolean isOnline);
}
