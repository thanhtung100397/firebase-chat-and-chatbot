package com.ttt.chat_module.views.auth.login;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface LoginView {
    void showProgress();
    void hideProgress();
    void showEmailInputError(String message);
    void showPasswordInpuitError(String message);
    void navigateToHomeScreen();
}
