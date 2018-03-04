package com.ttt.chat_module.views.main.friends;

import com.ttt.chat_module.models.User;

import java.util.List;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface FriendsView {
    void showRefreshingProgress();
    void hideRefreshingProgress();

    void showLoadingMoreProgress();
    void hideLoadingMoreProgress();

    void enableLoadingMore(boolean enable);
    void enableRefreshing(boolean enable);

    void refreshUsers(List<User> users);
    void addMoreUsers(List<User> users);
}
