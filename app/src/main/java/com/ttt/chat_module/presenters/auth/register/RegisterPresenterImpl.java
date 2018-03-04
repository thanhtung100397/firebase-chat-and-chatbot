package com.ttt.chat_module.presenters.auth.register;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.views.auth.register.RegisterView;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    public static final String TAG = "RegisterPresenterImpl";

    private Context context;
    private RegisterView registerView;
    private RegisterInteractor registerInteractor;

    public RegisterPresenterImpl(Context context, RegisterView registerView) {
        this.context = context;
        this.registerView = registerView;
        this.registerInteractor = new RegisterInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        context = null;
        registerInteractor.onViewDestroy();
    }

    @Override
    public void validateRegisterForm(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (firstName.isEmpty()) {
            registerView.showFirstNameInputError(context.getString(R.string.enter_first_name));
            return;
        }
        firstName = firstName.trim();

        if (lastName.isEmpty()) {
            registerView.showLastNameInputError(context.getString(R.string.enter_last_name));
            return;
        }
        lastName = lastName.trim();

        if (email.isEmpty()) {
            registerView.showEmailInputError(context.getString(R.string.enter_email));
            return;
        }

        email = email.trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerView.showEmailInputError(context.getString(R.string.invalid_email));
            return;
        }

        if (password.isEmpty()) {
            registerView.showPasswordInputError(context.getString(R.string.enter_password));
            return;
        }

        if (confirmPassword.isEmpty()) {
            registerView.showConfirmPasswordInputError(context.getString(R.string.enter_confirm_password));
            return;
        }

        if (!confirmPassword.equals(password)) {
            registerView.showConfirmPasswordInputError(context.getString(R.string.confirm_password_mismatch));
            return;
        }

        registerView.showProgress();
        registerInteractor.register(email, password, firstName, lastName, new OnRegisterCompleteListener() {
            @Override
            public void onRegisterSuccess(String email, String password) {
                registerView.hideProgress();
                registerView.navigateToLoginScreen(email, password);
            }

            @Override
            public void onEmailExist() {
                registerView.hideProgress();
                registerView.showEmailInputError(context.getString(R.string.email_exist));
            }

            @Override
            public void onPasswordWeek() {
                registerView.hideProgress();
                registerView.showPasswordInputError(context.getString(R.string.password_should_be_at_leas_6_characters));
            }

            @Override
            public void onError(String message) {
                Log.i(TAG, "onError: " + message);
                registerView.hideProgress();
                ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
            }
        });
    }
}
