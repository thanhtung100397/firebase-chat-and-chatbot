package com.ttt.chat_module.presenters.chat.activity;

import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.BaseInteractor;

import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface ChatActivityInteractor extends BaseInteractor {
    void findChatRoom(List<User> users, OnGetChatRoomIDCompleteListener listener);
    void createChatRoom(List<User> users, OnGetChatRoomIDCompleteListener listener);
}
