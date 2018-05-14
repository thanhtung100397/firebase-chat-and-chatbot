package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.BasePaginationListener;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnGetFriendsCompleteListener extends BasePaginationListener<DocumentSnapshot> {
    void onGetFriendsSuccess(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo);
}
