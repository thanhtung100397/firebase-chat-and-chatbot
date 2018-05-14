package com.ttt.chat_module.presenters.chat_bot;

import com.ttt.chat_module.presenters.BasePresenter;

public interface ChatBotPresenter extends BasePresenter {
    void registerApiAiService();
    void sendQueryMessage(String message);
    void handleChatBotPendingAction();
}
