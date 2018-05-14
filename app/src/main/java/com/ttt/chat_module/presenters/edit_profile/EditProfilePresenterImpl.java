package com.ttt.chat_module.presenters.edit_profile;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.UserProfile;
import com.ttt.chat_module.views.edit_profile.EditProfileView;

import java.util.Date;
import java.util.Map;

/**
 * Created by TranThanhTung on 03/02/2018.
 */

public class EditProfilePresenterImpl implements EditProfilePresenter {
    private Context context;
    private EditProfileView editProfileView;
    private EditProfileInteractor editProfileInteractor;

    public EditProfilePresenterImpl(Context context,
                                    EditProfileView editProfileView) {
        this.context = context;
        this.editProfileView = editProfileView;
        this.editProfileInteractor = new EditProfileInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        this.editProfileInteractor.onViewDestroy();
    }

    @Override
    public void validateUserProfile(Map<String, Uri> images, String userID,
                                    String avatarUrl, String coverUrl,
                                    String firstName, String lastName,
                                    Date birthday,
                                    String email,
                                    String phone) {
        if (lastName.isEmpty()) {
            editProfileView.showLastNameInputError(context.getString(R.string.enter_last_name));
            return;
        }

        if (firstName.isEmpty()) {
            editProfileView.showFistNameInputError(context.getString(R.string.enter_first_name));
            return;
        }

        if (email.isEmpty()) {
            editProfileView.showEmailInputError(context.getString(R.string.enter_email));
            return;
        }

        email = email.trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            editProfileView.showEmailInputError(context.getString(R.string.invalid_email));
            return;
        }

        if(!phone.isEmpty() && !phone.matches(Constants.PHONE_REGEX)) {
            editProfileView.showPhoneInputError(context.getString(R.string.invalid_phone));
            return;
        }

        UserProfile userProfile = new UserProfile(userID,
                avatarUrl, coverUrl,
                firstName, lastName,
                birthday, email, phone);

        editProfileView.showProgress();
        if (images.size() != 0) {
            uploadImagesThenSaveProfile(userID, images, userProfile);
        } else {
            updateUserProfile(userProfile);
        }
    }

    private void updateUserProfile(UserProfile userProfile) {
        editProfileInteractor
                .updateUserProfile(userProfile, new OnUpdateUserProfileCompleteListener() {

                    @Override
                    public void onUpdateUserProfileSuccess(UserProfile userProfile) {
                        editProfileView.hideProgress();
                        UserAuth.saveProfile(context, userProfile);
                        editProfileView.navigateToProfileScreen(userProfile);
                    }

                    @Override
                    public void onRequestError(String message) {
                        editProfileView.hideProgress();
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }

    private void uploadImagesThenSaveProfile(String userID, Map<String, Uri> images, UserProfile userProfile) {
        editProfileInteractor.uploadImages(images, userID, mapUrls -> {
                    String avatarUrl = mapUrls.get(Constants.FIREBASE_AVATAR_FILE_NAME);
                    if (avatarUrl != null) {
                        userProfile.setAvatarUrl(avatarUrl);
                    }

                    String coverUrl = mapUrls.get(Constants.FIREBASE_COVER_FILE_NAME);
                    if (coverUrl != null) {
                        userProfile.setCoverUrl(coverUrl);
                    }

                    updateUserProfile(userProfile);
                },
                (name, e) -> {
                    editProfileView.hideProgress();
                    ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                });
    }
}
