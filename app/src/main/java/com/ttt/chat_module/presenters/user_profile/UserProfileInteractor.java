package com.ttt.chat_module.presenters.user_profile;

import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.splash.OnGetUserCompleteListener;

public interface UserProfileInteractor extends BaseInteractor {
    void getUser(String userID, OnGetUserCompleteListener listener);
}
