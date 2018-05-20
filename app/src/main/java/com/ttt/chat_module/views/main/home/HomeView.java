package com.ttt.chat_module.views.main.home;

import com.ttt.chat_module.common.adapter.recycler_view_adapter.UserInfoChatRooms;
import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 19/02/2018.
 */

public interface HomeView {
    void showRefreshingProgress();
    void hideRefreshingProgress();
    void enableRefreshing(boolean enable);
    void refreshChatRooms(Map<String, UserInfoChatRooms> userInfoChatRoomsMap, List<ChatRoom> chatRooms);

    void showLoadingMoreProgress();
    void hideLoadingMoreProgress();
    void enableLoadingMore(boolean enable);
    void addMoreChatRooms(Map<String, UserInfoChatRooms> userInfoChatRoomsMap, List<ChatRoom> chatRooms);

    void updateChatRoom(ChatRoomInfo chatRoomInfo);

}
