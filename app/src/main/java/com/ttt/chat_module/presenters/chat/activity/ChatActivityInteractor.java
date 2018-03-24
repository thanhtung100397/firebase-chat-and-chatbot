package com.ttt.chat_module.presenters.chat.activity;

import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface ChatActivityInteractor extends BaseInteractor {
    void findChatRoom(String[] userIDs, OnGetChatRoomIDCompleteListener listener);
    void createChatRoom(String[] userIDs, OnGetChatRoomIDCompleteListener listener);
    void getUsersInfo(String[] userIDs, OnGetUsersInfoCompleteListener listener);
}
