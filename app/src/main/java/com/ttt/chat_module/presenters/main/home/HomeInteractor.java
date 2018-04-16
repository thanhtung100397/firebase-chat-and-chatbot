package com.ttt.chat_module.presenters.main.home;

import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.presenters.BaseInteractor;

public interface HomeInteractor extends BaseInteractor {
    void getChatRooms(ChatRoomInfo startAt, int pageSize, String userID,
                      OnGetChatRoomsCompleteListener listener);
}
