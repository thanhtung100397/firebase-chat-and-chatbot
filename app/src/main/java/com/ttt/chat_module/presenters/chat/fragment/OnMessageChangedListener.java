package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.presenters.chat.BasePaginationListener;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface OnMessageChangedListener extends BasePaginationListener<TextMessage> {
    void onMessageAdded(TextMessage textMessage);
    void onMessageModified(TextMessage textMessage, int position);
}
