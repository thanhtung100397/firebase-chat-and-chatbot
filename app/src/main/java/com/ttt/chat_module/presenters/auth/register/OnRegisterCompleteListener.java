package com.ttt.chat_module.presenters.auth.register;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnRegisterCompleteListener {
    void onRegisterSuccess(String email);
    void onEmailExist();
    void onPasswordWeek();
    void onError(String message);
}
