package com.ttt.chat_module.views.chat_bot;

import com.ttt.chat_module.models.chat_bot_message.ContactQueryMessage;

public interface ChatBotActivityView {
    void showLoadingProgress();
    void hideLoadingProgress();
    void showChatBotMessage(String message);
    void showChatBotMessageAndItems(ContactQueryMessage message);
    void showChatBotPermissionDeniedMessage();
    void showUserMessage(String message);
    void clearInputMessage();
    boolean requestPhoneCallPermission();
    boolean requestCameraPermission();
    boolean requestReadContactsPermission();
    boolean requestReadContactsAndPhoneCallPermission();
}
