package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.chat.BasePaginationListener;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnGetFriendsCompleteListener extends BasePaginationListener<DocumentSnapshot> {
    void onGetFriendsSuccess(List<User> users);
}
