package com.ttt.chat_module.views.edit_profile;

import com.ttt.chat_module.models.UserProfile;

/**
 * Created by TranThanhTung on 05/02/2018.
 */

public interface EditProfileView {
    void showFistNameInputError(String message);
    void showLastNameInputError(String message);
    void showEmailInputError(String message);
    void showPhoneInputError(String message);

    void showProgress();
    void hideProgress();

    void navigateToProfileScreen(UserProfile userProfile);
}
