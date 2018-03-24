package com.ttt.chat_module.presenters.chat.activity;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface OnGetChatRoomIDCompleteListener {
    void onGetChatRoomIDSuccess(String chatRoomID);
    void onGetChatRoomIDFailure(String message);
}
