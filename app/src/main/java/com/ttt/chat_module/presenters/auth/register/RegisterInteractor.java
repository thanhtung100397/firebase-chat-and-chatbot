package com.ttt.chat_module.presenters.auth.register;

import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface RegisterInteractor extends BaseInteractor {
    void register(String email, String password, String firstName, String lastName, OnRegisterCompleteListener listener);
}
