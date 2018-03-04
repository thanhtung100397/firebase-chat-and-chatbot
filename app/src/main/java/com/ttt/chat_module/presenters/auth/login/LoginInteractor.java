package com.ttt.chat_module.presenters.auth.login;

import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface LoginInteractor extends BaseInteractor {
    void login(String username, String password, OnLoginCompleteListener onLoginCompleteListener);
}
