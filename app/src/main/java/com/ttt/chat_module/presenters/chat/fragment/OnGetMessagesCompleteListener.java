package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.presenters.chat.BasePaginationListener;

import java.util.List;

/**
 * Created by TranThanhTung on 21/03/2018.
 */

public interface OnGetMessagesCompleteListener extends BasePaginationListener<TextMessage> {
    void onGetMessageSuccess(List<TextMessage> textMessages);
}
