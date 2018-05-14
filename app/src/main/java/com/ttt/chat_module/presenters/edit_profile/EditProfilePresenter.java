package com.ttt.chat_module.presenters.edit_profile;

import android.net.Uri;

import com.ttt.chat_module.presenters.BasePresenter;

import java.util.Date;
import java.util.Map;

/**
 * Created by TranThanhTung on 03/02/2018.
 */

public interface EditProfilePresenter extends BasePresenter {
    void validateUserProfile(Map<String, Uri> images, String userID,
                             String avatarUrl, String coverUrl,
                             String firstName, String lastName,
                             Date birthday,
                             String email,
                             String phone);
}
