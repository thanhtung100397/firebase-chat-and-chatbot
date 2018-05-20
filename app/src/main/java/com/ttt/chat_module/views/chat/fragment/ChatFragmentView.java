package com.ttt.chat_module.views.chat.fragment;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.TextMessage;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentView {
    void addTopMessage(BaseMessage baseMessage);
    void addMessages(List<BaseMessage> textMessages);
    void updateMessageState(BaseMessage baseMessage, int position);
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

    void updateFriend(UserInfo userInfo, boolean isOnline);

    void updateNotificationMenuItem(boolean enableNotification);
}
