package com.ttt.chat_module.presenters.chat.activity;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.BaseInteractor;

import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface ChatActivityInteractor extends BaseInteractor {
    void getChatRoomInfo(String chatRoomID, OnGetChatRoomInfoCompleteListener listener);
    void findChatRoom(List<UserInfo> usersInfo, OnGetChatRoomInfoCompleteListener listener);
    void createChatRoom(List<UserInfo> usersInfo, OnGetChatRoomInfoCompleteListener listener);
}
