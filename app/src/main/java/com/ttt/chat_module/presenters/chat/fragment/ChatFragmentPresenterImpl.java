package com.ttt.chat_module.presenters.chat.fragment;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.Message;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.views.chat.fragment.ChatFragmentView;

import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentPresenterImpl implements ChatFragmentPresenter {
    public static final String TAG = "ChatFrgPresenterImpl";

    private ChatFragmentView chatFragmentView;
    private ChatFragmentInteractor chatFragmentInteractor;

    private String roomID;
    private UserInfo ownerInfo;
    private List<UserInfo> friendsInfo;

    private DocumentSnapshot lastMessageDocumentSnapshot = null;

    public ChatFragmentPresenterImpl(ChatFragmentView chatFragmentView,
                                     String roomID,
                                     List<UserInfo> usersInfo) {
        this.chatFragmentView = chatFragmentView;
        this.chatFragmentInteractor = new ChatFragmentInteractorImpl();

        this.roomID = roomID;
        if (!usersInfo.isEmpty()) {
            this.ownerInfo = usersInfo.remove(0);
        }
        this.friendsInfo = usersInfo;
    }

    private boolean isUserMessage(Message message) {
        return message.getOwnerEmail().equals(ownerInfo.getEmail());
    }

    public boolean isCoupleChatRoom() {
        return friendsInfo.size() == 1;
    }

    @Override
    public void onViewDestroy() {
        chatFragmentInteractor.onViewDestroy();
    }

    @Override
    public void registerOnMessageAddedListener() {
        chatFragmentInteractor.registerOnMessageChangedListener(roomID, new OnMessageChangedListener() {
            @Override
            public void onMessageAdded(Message message) {
                if (isUserMessage(message)) {
                    chatFragmentView.addOwnerMessage(message);
                } else {
                    chatFragmentView.addFriendMessage(message);
                }
            }

            @Override
            public void onMessageModified(Message message) {

            }
        });
    }

    @Override
    public void unregisterOnMessageAddedListener() {
        chatFragmentInteractor.unregisterOnMessageChangedListener();
    }

    @Override
    public void validateSendingMessage(String message) {
        if (message.isEmpty()) {
            return;
        }
        chatFragmentInteractor.sendMessage(roomID, message, new OnRequestCompleteListener() {
            @Override
            public void onRequestSuccess() {

            }

            @Override
            public void onRequestError(String message) {
                Log.i(TAG, "onRequestError: " + message);
            }
        });
    }

    @Override
    public void fetchLatestMessages() {
        chatFragmentView.showFirstLoadingMessagesProgress();
        chatFragmentInteractor.getMessages(roomID, lastMessageDocumentSnapshot,
                Constants.PAGE_SIZE, new OnEachMessagesFetchedListener() {
                    @Override
                    public void onEachMessageFetched(Message message) {

                    }

                    @Override
                    public void onFetchMessagesCompleted() {
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                    }

                    @Override
                    public void onServerError(String message) {
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                        chatFragmentView.showFirstLoadingMessagesError();
                    }

                    @Override
                    public void onLastDocumentSnapshotFetched(DocumentSnapshot lastDocumentSnapshot) {
                        lastMessageDocumentSnapshot = lastDocumentSnapshot;
                    }
                });
    }

    @Override
    public UserInfo getOwnerInfo() {
        return ownerInfo;
    }

    @Override
    public List<UserInfo> getFriendsInfo() {
        return friendsInfo;
    }
}
