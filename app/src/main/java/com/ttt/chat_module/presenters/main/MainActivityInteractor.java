package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.presenters.BaseInteractor;

public interface MainActivityInteractor extends BaseInteractor {
    void registerUsersOnlineStateChangeListener(OnUsersOnlineStateChangeListener listener);
    void unregisterUsersOnlineStateChangeListener();

    void registerChatRoomLastMessageChangeListener(OnChatRoomLastMessageChangeListener listener);
    void unregisterChatRoomLastMessageChangeListener();
}
