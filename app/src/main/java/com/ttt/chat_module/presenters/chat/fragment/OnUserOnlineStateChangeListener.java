package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnUserOnlineStateChangeListener extends BaseRequestListener {
    void onOnlineStateChanged(boolean isOnline);
}
