package com.ttt.chat_module.presenters.auth.login;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface OnLoginCompleteListener extends BaseRequestListener{
    void onLoginSuccess(User user);
    void onWrongEmailOrPassword();
}
