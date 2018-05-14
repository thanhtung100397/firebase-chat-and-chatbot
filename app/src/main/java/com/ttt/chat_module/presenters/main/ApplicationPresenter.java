package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.presenters.BasePresenter;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 23/03/2018.
 */

public interface ApplicationPresenter extends BasePresenter {
    void changeOnlineState(boolean isOnline, OnRequestCompleteListener listener);
}
