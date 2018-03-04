package com.ttt.chat_module.presenters.auth.login;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface LoginPresenter extends BasePresenter{
    void validateEmailAndPassword(String email, String password);
}
