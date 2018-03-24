package com.ttt.chat_module.presenters.main;

import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.presenters.ApplicationInteractor;
import com.ttt.chat_module.presenters.ApplicationInteractorImpl;
import com.ttt.chat_module.presenters.ApplicationPresenter;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 23/03/2018.
 */

public class ApplicationPresenterImpl implements ApplicationPresenter {
    private ApplicationInteractor applicationInteractor;

    public ApplicationPresenterImpl() {
        this.applicationInteractor = new ApplicationInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        applicationInteractor.onViewDestroy();
    }

    @Override
    public void changeOnlineState(boolean isOnline, OnRequestCompleteListener listener) {
        String userID = UserAuth.getUserID();
        if (userID != null) {
            applicationInteractor.updateUserOnlineState(UserAuth.getUserID(), isOnline, listener);
        }
    }
}
