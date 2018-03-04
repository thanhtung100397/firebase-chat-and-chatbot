package com.ttt.chat_module.presenters.main.friends;

import com.ttt.chat_module.presenters.BasePresenter;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface FriendsPresenter extends BasePresenter {
    void refreshFriends();
    void loadMoreFriends();
}
