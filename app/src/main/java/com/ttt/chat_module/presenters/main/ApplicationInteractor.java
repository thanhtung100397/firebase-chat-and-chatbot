package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 23/03/2018.
 */

public interface ApplicationInteractor extends BaseInteractor {
    void updateUserOnlineState(String userID, boolean isOnline, OnRequestCompleteListener listener);
}
