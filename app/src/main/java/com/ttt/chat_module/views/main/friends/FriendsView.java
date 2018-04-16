package com.ttt.chat_module.views.main.friends;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;

import java.util.List;
import java.util.Map;

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

    void refreshUsers(Map<String, Integer> userPositionMap, List<UserInfo> usersInfo);
    void addMoreUsers(Map<String,Integer> userPositionMap, List<UserInfo> usersInfo);
}
