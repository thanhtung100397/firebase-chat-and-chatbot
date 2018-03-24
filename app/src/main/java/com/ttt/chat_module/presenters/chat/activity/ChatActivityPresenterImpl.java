package com.ttt.chat_module.presenters.chat.activity;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.base_progress.BaseProgressActivityPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranThanhTung on 12/03/2018.
 */

public class ChatActivityPresenterImpl extends BaseProgressActivityPresenter {
    private ChatActivityInteractor chatActivityInteractor;
    private String[] userIDs;

    public ChatActivityPresenterImpl(String[] userIDs) {
        this.userIDs = userIDs;
        this.chatActivityInteractor = new ChatActivityInteractorImpl();
    }

    @Override
    public void onViewDestroy() {
        chatActivityInteractor.onViewDestroy();
    }

    @Override
    public void fetchData(OnFetchDataProgressListener listener) {
        chatActivityInteractor.findChatRoom(userIDs, new OnGetChatRoomIDCompleteListener() {
            @Override
            public void onGetChatRoomIDSuccess(String chatRoomID) {
                if (chatRoomID == null) {
                    createNewChatRoom(userIDs, listener);
                } else {
                    fetchUserInfo(chatRoomID, userIDs, listener);
                }
            }

            @Override
            public void onGetChatRoomIDFailure(String message) {
                listener.onFetchDataFailure(message);
            }
        });
    }

    private void createNewChatRoom(String[] userIDs, OnFetchDataProgressListener listener) {
        chatActivityInteractor.createChatRoom(userIDs, new OnGetChatRoomIDCompleteListener() {
            @Override
            public void onGetChatRoomIDSuccess(String chatRoomID) {
                fetchUserInfo(chatRoomID, userIDs, listener);
            }

            @Override
            public void onGetChatRoomIDFailure(String message) {
                listener.onFetchDataFailure(message);
            }
        });
    }

    private void fetchUserInfo(String roomID, String[] userIDs, OnFetchDataProgressListener listener) {
        chatActivityInteractor.getUsersInfo(userIDs, new OnGetUsersInfoCompleteListener() {
            @Override
            public void onGetUsersInfoSuccess(List<UserInfo> usersInfo) {
                listener.onFetchDataSuccess(createBundle(roomID, usersInfo));
            }

            @Override
            public void onServerError(String message) {
                listener.onFetchDataFailure(message);
            }
        });
    }

    private Bundle createBundle(String roomID, List<UserInfo> usersInfo) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ROOM_ID, roomID);
        bundle.putParcelableArrayList(Constants.KEY_USERS_INFO, (ArrayList<? extends Parcelable>) usersInfo);
        return bundle;
    }
}
