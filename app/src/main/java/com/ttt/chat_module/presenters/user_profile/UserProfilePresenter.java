package com.ttt.chat_module.presenters.user_profile;

import com.ttt.chat_module.presenters.BasePresenter;

public interface UserProfilePresenter extends BasePresenter {
    void fetchUserProfile(String userID);
}
