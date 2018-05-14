package com.ttt.chat_module.presenters.user_profile;

import android.content.Context;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.splash.OnGetUserCompleteListener;
import com.ttt.chat_module.views.user_profile.UserProfileView;

public class UserProfilePresenterImpl implements UserProfilePresenter {
    private Context context;
    private UserProfileView userProfileView;
    private UserProfileInteractor userProfileInteractor;

    public UserProfilePresenterImpl(Context context, UserProfileView userProfileView) {
        this.context = context;
        this.userProfileView = userProfileView;
        this.userProfileInteractor = new UserProfileInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        this.userProfileInteractor.onViewDestroy();
    }

    @Override
    public void fetchUserProfile(String userID) {
        userProfileInteractor.getUser(userID, new OnGetUserCompleteListener() {
            @Override
            public void onGetUserSuccess(User user) {
                userProfileView.showUserProfile(user);
            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
            }
        });
    }
}
