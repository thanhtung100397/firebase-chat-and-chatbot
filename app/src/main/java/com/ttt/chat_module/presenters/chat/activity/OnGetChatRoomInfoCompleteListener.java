package com.ttt.chat_module.presenters.chat.activity;

import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.presenters.BaseRequestListener;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public interface OnGetChatRoomInfoCompleteListener extends BaseRequestListener {
    void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo);
}
