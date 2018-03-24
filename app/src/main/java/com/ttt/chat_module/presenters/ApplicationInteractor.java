package com.ttt.chat_module.presenters;

/**
 * Created by TranThanhTung on 23/03/2018.
 */

public interface ApplicationInteractor extends BaseInteractor {
    void updateUserOnlineState(String userID, boolean isOnline, OnRequestCompleteListener listener);
}
