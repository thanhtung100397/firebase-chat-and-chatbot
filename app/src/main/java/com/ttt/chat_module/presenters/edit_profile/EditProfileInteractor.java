package com.ttt.chat_module.presenters.edit_profile;

import android.net.Uri;

import com.ttt.chat_module.common.utils.FirebaseUploadImageHelper;
import com.ttt.chat_module.models.UserProfile;
import com.ttt.chat_module.presenters.BaseInteractor;

import java.util.Map;

/**
 * Created by TranThanhTung on 03/02/2018.
 */

public interface EditProfileInteractor extends BaseInteractor {
    void updateUserProfile(UserProfile userProfile, OnUpdateUserProfileCompleteListener listener);

    void uploadImages(Map<String, Uri> images,
                      String dir,
                      FirebaseUploadImageHelper.OnCompleteListener completeListener,
                      FirebaseUploadImageHelper.OnFailureListener failureListener);
}
