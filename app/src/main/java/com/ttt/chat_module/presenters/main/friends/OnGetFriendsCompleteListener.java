package com.ttt.chat_module.presenters.main.friends;

import com.ttt.chat_module.models.User;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnGetFriendsCompleteListener {
    void onGetFriendsSuccess(List<User> users);
    void onError(String message);
}
