package com.ttt.chat_module.presenters.chat.activity;

import com.ttt.chat_module.models.UserInfo;

import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface OnGetUsersInfoCompleteListener {
    void onGetUsersInfoSuccess(List<UserInfo> usersInfo);
    void onServerError(String message);
}
