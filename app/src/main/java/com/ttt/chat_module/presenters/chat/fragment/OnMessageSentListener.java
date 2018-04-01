package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 26/03/2018.
 */

public interface OnMessageSentListener extends BaseRequestListener {
    void onMessageSent(TextMessage textMessage);
}
