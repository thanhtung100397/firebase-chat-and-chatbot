package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnUserChangeListener extends BaseRequestListener {
    void onUserChanged(User user);
}
