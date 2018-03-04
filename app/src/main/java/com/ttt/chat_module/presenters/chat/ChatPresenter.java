package com.ttt.chat_module.presenters.chat;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatPresenter extends BasePresenter {
    void registerOnMessageAddedListener();
    void unregisterOnMessageAddedListener();

    void validateSendingMessage(String message);
}
