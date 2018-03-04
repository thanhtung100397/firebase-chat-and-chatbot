package com.ttt.chat_module.presenters.main.friends;

import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface FriendsInteractor extends BaseInteractor {
    void getFriends(int pageIndex, int pageSize, OnGetFriendsCompleteListener listener);
}
