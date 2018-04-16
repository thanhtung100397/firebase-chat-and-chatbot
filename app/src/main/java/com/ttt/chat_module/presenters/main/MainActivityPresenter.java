package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.presenters.BasePresenter;

public interface MainActivityPresenter extends BasePresenter{
    void registerUsersOnlineStateChangeListener();
    void unregisterUsersOnlineStateChangeListener();

    void registerChatRoomLastMessageChangeListener();
    void unregisterChatRoomLastMessageChangeListener();
}
