package com.ttt.chat_module.presenters.chat_bot;

import com.ttt.chat_module.models.ContactInfo;
import com.ttt.chat_module.presenters.BaseInteractor;

import java.util.List;

import ai.api.AIListener;

public interface ChatBotInteractor extends BaseInteractor {
    void initApiAiService();
    void query(String message,OnBotResponseListener listener);
    List<ContactInfo> searchContacts(String query);
}
