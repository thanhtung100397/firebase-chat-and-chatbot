package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.Message;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnMessageChangedListener {
    void onMessageAdded(Message message);
    void onMessageModified(Message message);
}
