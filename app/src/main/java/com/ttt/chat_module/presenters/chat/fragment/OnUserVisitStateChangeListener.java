package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnUserVisitStateChangeListener extends BaseRequestListener {
    void onUserVisitStateChanged(String userID, VisitState visitState);
}
