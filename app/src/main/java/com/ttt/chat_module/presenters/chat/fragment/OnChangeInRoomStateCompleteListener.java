package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.presenters.BaseRequestListener;

public interface OnChangeInRoomStateCompleteListener extends BaseRequestListener {
    void onChangeInRoomStateSuccess(boolean inRoomState);
}
