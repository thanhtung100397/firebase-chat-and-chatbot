package com.ttt.chat_module.presenters.main.home;

import com.ttt.chat_module.presenters.BasePresenter;

public interface HomePresenter extends BasePresenter {
    void refreshChatRooms();
    void loadMoreChatRooms();
}
