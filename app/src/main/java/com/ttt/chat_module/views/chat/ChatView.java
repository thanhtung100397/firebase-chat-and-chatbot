package com.ttt.chat_module.views.chat;

import com.ttt.chat_module.common.adapter.recycler_view_adapter.Message;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatView {
    void addOwnerMessage(Message message);
    void addFriendMessage(Message message);
    void onMessageSeen();
}
