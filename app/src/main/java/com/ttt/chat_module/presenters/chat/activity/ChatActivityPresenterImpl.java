package com.ttt.chat_module.presenters.chat.activity;

import android.os.Bundle;
import android.util.Log;

import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;

import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatActivityPresenterImpl extends BaseProgressActivityPresenter {
    private ChatActivityInteractor chatActivityInteractor;
    private List<User> users;

    public ChatActivityPresenterImpl(List<User> users) {
        this.users = users;
        this.chatActivityInteractor = new ChatActivityInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        chatActivityInteractor.onViewDestroy();
    }

    @Override
    public void fetchData(OnFetchDataProgressListener listener) {
        chatActivityInteractor.findChatRoom(users, new OnGetChatRoomIDCompleteListener() {

            @Override
            public void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo) {
                if (chatRoomInfo == null) {
                    createNewChatRoom(users, listener);
                } else {
                    listener.onFetchDataSuccess(createBundle(chatRoomInfo));
                }
            }

            @Override
            public void onRequestError(String message) {
                listener.onFetchDataFailure(message);
            }
        });
    }

    private void createNewChatRoom(List<User> users, OnFetchDataProgressListener listener) {
        chatActivityInteractor.createChatRoom(users, new OnGetChatRoomIDCompleteListener() {
            @Override
            public void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo) {
                listener.onFetchDataSuccess(createBundle(chatRoomInfo));
            }

            @Override
            public void onRequestError(String message) {
                Log.i("ABC", "onRequestError: "+message);
                listener.onFetchDataFailure(message);
            }
        });
    }

    private Bundle createBundle(ChatRoomInfo chatRoomInfo) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_OWNER_ID, users.get(0).getId());
        bundle.putSerializable(Constants.KEY_CHAT_ROOM_INFO, chatRoomInfo);
        return bundle;
    }
}
