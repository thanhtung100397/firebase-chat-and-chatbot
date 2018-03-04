package com.ttt.chat_module.presenters.chat;

import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatInteractor extends BaseInteractor {
    void sendMessage(String message, OnRequestCompleteListener listener);
    void registerOnMessageChangedListener(OnMessageChangedListener listener);
    void unregisterOnMessageChangedListener();
}
