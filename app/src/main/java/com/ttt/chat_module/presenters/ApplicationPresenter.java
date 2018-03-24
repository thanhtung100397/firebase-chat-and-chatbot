package com.ttt.chat_module.presenters;

/**
 * Created by TranThanhTung on 23/03/2018.
 */

public interface ApplicationPresenter extends BasePresenter {
    void changeOnlineState(boolean isOnline, OnRequestCompleteListener listener);
}
