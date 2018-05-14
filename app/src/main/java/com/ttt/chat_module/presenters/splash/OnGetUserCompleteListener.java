package com.ttt.chat_module.presenters.splash;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnGetUserCompleteListener extends BaseRequestListener {
    void onGetUserSuccess(User user);
}
