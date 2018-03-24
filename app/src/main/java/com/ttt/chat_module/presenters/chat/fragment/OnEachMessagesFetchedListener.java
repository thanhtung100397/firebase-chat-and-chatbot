package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.Message;
import com.ttt.chat_module.presenters.chat.BasePaginationListener;

/**
 * Created by TranThanhTung on 21/03/2018.
 */

public interface OnEachMessagesFetchedListener extends BasePaginationListener {
    void onEachMessageFetched(Message message);
    void onFetchMessagesCompleted();
    void onServerError(String message);
}
