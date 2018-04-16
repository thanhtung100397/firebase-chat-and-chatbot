package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnSendMessageCompleteListener extends BaseRequestListener{
    void onSendMessageSuccess(BaseMessage message);
}
