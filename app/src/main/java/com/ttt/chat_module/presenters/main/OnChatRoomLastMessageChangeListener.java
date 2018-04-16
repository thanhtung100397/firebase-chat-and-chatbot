package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.presenters.BaseRequestListener;

import java.util.Map;

public interface OnChatRoomLastMessageChangeListener extends BaseRequestListener {
    void onChatRoomLastMessageChanged(String roomID, Map<String, Object> lastMessage);
}
