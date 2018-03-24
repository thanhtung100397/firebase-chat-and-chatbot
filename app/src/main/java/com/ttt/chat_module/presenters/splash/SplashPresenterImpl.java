package com.ttt.chat_module.presenters.splash;

import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.views.splash.SplashView;

/**
 * Created by TranThanhTung on 25/01/2018.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
    }

    @Override
    public void goToOnlineState() {
        splashInteractor.updateUserOnlineState(UserAuth.getUserID(), true, new OnRequestCompleteListener() {
            @Override
            public void onRequestSuccess() {
                splashView.completeLoading();
            }

            @Override
            public void onRequestError(String message) {
                splashView.showErrorDialog();
            }
        });
    }
}
