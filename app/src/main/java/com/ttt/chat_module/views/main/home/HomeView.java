package com.ttt.chat_module.views.main.home;

import com.ttt.chat_module.models.ChatRoom;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface HomeView {
    void showRefreshingProgress();
    void hideRefreshingProgress();
    void enableRefreshing(boolean enable);
    void refreshChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms);

    void showLoadingMoreProgress();
    void hideLoadingMoreProgress();
    void enableLoadingMore(boolean enable);
    void addMoreChatRooms(Map<String, Integer> roomPositionMap, List<ChatRoom> chatRooms);

}
