package com.ttt.chat_module.presenters.auth.register;

import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnRegisterCompleteListener extends BaseRequestListener {
    void onRegisterSuccess(String email);
    void onEmailExist();
    void onPasswordWeek();
}
