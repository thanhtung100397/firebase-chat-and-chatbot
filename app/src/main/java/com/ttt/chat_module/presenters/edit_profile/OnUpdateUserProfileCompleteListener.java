package com.ttt.chat_module.presenters.edit_profile;

import com.ttt.chat_module.models.UserProfile;
import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 04/02/2018.
 */

public interface OnUpdateUserProfileCompleteListener extends BaseRequestListener {
    void onUpdateUserProfileSuccess(UserProfile userProfile);
}
