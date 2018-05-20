package com.ttt.chat_module.presenters.main.home;

import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnChatRoomChangeListener extends BaseRequestListener {
    void onChatRoomChanged(ChatRoomInfo chatRoomInfo);
}
