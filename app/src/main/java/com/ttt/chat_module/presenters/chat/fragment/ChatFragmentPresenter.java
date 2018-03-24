package com.ttt.chat_module.presenters.chat.fragment;

import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.BasePresenter;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentPresenter extends BasePresenter {
    boolean isCoupleChatRoom();
    void registerOnMessageAddedListener();
    void unregisterOnMessageAddedListener();
    void validateSendingMessage(String message);
    void fetchLatestMessages();
    UserInfo getOwnerInfo();
    List<UserInfo> getFriendsInfo();
}
