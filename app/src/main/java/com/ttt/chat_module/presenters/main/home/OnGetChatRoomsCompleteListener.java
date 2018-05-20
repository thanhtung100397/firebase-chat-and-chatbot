package com.ttt.chat_module.presenters.main.home;

import com.ttt.chat_module.models.ChatRoom;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.presenters.BasePaginationListener;

import java.util.List;
import java.util.Map;

public interface OnGetChatRoomsCompleteListener extends BasePaginationListener<ChatRoomInfo> {
    void onGetChatRoomsSuccess(List<ChatRoom> chatRooms);
}
