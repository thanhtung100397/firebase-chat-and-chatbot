package com.ttt.chat_module.presenters.chat.activity;

import android.content.Context;
import android.os.Bundle;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatActivityPresenterImpl extends BaseProgressActivityPresenter {
    private Context context;
    private ChatActivityInteractor chatActivityInteractor;
    private String ownerID;
    private List<UserInfo> usersInfo;
    private String roomID;

    public ChatActivityPresenterImpl(Context context, List<UserInfo> usersInfo) {
        this.context = context;
        this.usersInfo = usersInfo;
        this.ownerID = usersInfo.get(0).getId();
        this.chatActivityInteractor = new ChatActivityInteractorImpl();
    }

    public ChatActivityPresenterImpl(Context context, String roomID) {
        this.context = context;
        this.roomID = roomID;
        this.ownerID = UserAuth.getUserID();
        this.chatActivityInteractor = new ChatActivityInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        chatActivityInteractor.onViewDestroy();
    }

    @Override
    public void fetchData(OnFetchDataProgressListener listener) {
        if (roomID == null) {
            chatActivityInteractor.findChatRoom(usersInfo, new OnGetChatRoomInfoCompleteListener() {
                @Override
                public void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo) {
                    if (chatRoomInfo == null) {
                        createNewChatRoom(usersInfo, listener);
                    } else {
                        listener.onFetchDataSuccess(createBundle(chatRoomInfo));
                    }
                }

                @Override
                public void onRequestError(String message) {
                    listener.onFetchDataFailure(message);
                }
            });
        } else {
            chatActivityInteractor.getChatRoomInfo(roomID, new OnGetChatRoomInfoCompleteListener() {
                @Override
                public void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo) {
                    if (chatRoomInfo == null) {
                        onRequestError(context.getString(R.string.chat_room_not_found));
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
    }

    private void createNewChatRoom(List<UserInfo> usersInfo, OnFetchDataProgressListener listener) {
        chatActivityInteractor.createChatRoom(usersInfo, new OnGetChatRoomInfoCompleteListener() {
            @Override
            public void onGetChatRoomInfoSuccess(ChatRoomInfo chatRoomInfo) {
                listener.onFetchDataSuccess(createBundle(chatRoomInfo));
            }

            @Override
            public void onRequestError(String message) {
                listener.onFetchDataFailure(message);
            }
        });
    }

    private Bundle createBundle(ChatRoomInfo chatRoomInfo) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_OWNER_ID, ownerID);
        bundle.putSerializable(Constants.KEY_CHAT_ROOM_INFO, chatRoomInfo);
        return bundle;
    }
}
