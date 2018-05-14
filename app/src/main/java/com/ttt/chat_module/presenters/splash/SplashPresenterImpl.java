package com.ttt.chat_module.presenters.splash;

import android.content.Context;

import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.views.splash.SplashView;

/**
 * Created by TranThanhTung on 25/01/2018.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private Context context;
    private SplashView splashView;
    private SplashInteractor splashInteractor;

    public SplashPresenterImpl(Context context, SplashView splashView) {
        this.context = context;
        this.splashView = splashView;
        this.splashInteractor = new SplashInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
    }

    @Override
    public void goToOnlineState() {
        splashInteractor.updateUserOnlineStateAndFetchUser(UserAuth.getUserID(), true,
                new OnGetUserCompleteListener() {
                    @Override
                    public void onGetUserSuccess(User user) {
                        UserAuth.saveUser(context, user);
                        splashView.completeLoading();
                    }

                    @Override
                    public void onRequestError(String message) {
                        splashView.showErrorDialog();
                    }
                });
    }
}
