package com.ttt.chat_module.presenters.chat;

import com.ttt.chat_module.common.adapter.recycler_view_adapter.Message;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnMessageChangedListener {
    void onMessageAdded(Message message);
    void onMessageModified(Message message);
}
