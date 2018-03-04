package com.ttt.chat_module.views.auth.register;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void showFirstNameInputError(String message);
    void showLastNameInputError(String message);
    void showEmailInputError(String message);
    void showPasswordInputError(String message);
    void showConfirmPasswordInputError(String message);
    void navigateToLoginScreen(String email, String password);
}
