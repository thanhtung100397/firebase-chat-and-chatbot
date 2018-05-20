package com.ttt.chat_module.presenters.main.home;

import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnUsersOnlineStateChangeListener extends BaseRequestListener {
    void onUserOnlineStateChanged(String usedID, boolean isOnline);
}
