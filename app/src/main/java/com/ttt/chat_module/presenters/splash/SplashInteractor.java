package com.ttt.chat_module.presenters.splash;

import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 25/01/2018.
 */

public interface SplashInteractor extends BaseInteractor {
    void updateUserOnlineStateAndFetchUser(String userID, boolean isOnline, OnGetUserCompleteListener listener);
}
