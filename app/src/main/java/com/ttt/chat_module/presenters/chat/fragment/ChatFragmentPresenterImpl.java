package com.ttt.chat_module.presenters.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.services.SendImageMessageService;
import com.ttt.chat_module.views.chat.fragment.ChatFragmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentPresenterImpl implements ChatFragmentPresenter {
    private Context context;
    private ChatFragmentView chatFragmentView;
    private ChatFragmentInteractor chatFragmentInteractor;

    private String roomID;
    private UserInfo ownerInfo;
    private boolean hasNoMessageLeft;
    private Map<String, UserInfo> mapFriendsInfo;

    private TextMessage lastTextMessage = null;

    public ChatFragmentPresenterImpl(Context context,
                                     ChatFragmentView chatFragmentView,
                                     String ownerID,
                                     ChatRoomInfo chatRoomInfo) {
        this.context = context;
        this.chatFragmentView = chatFragmentView;
        this.chatFragmentInteractor = new ChatFragmentInteractorImpl();

        this.roomID = chatRoomInfo.getId();
        Map<String, UserInfo> usersInfo = chatRoomInfo.getUsersInfo();
        this.ownerInfo = usersInfo.remove(ownerID);
        this.mapFriendsInfo = usersInfo;
    }

    public boolean isCoupleChatRoom() {
        return mapFriendsInfo.size() == 1;
    }

    @Override
    public void onViewDestroy() {
        chatFragmentInteractor.onViewDestroy();
    }

    @Override
    public void registerOnMessageAddedListener() {
        chatFragmentView.showFirstLoadingMessagesProgress();
        chatFragmentInteractor.registerOnMessageChangedListener(roomID, Constants.PAGE_SIZE,
                new OnMessageChangedListener() {
                    @Override
                    public void onLastElementFetched(TextMessage element, boolean hasNoElementLeft) {
                        hasNoMessageLeft = hasNoElementLeft;
                        lastTextMessage = element;
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                    }

                    @Override
                    public void onMessageAdded(TextMessage textMessage) {
                        chatFragmentView.addTopMessage(textMessage);
                    }

                    @Override
                    public void onMessageModified(TextMessage textMessage, int position) {
                        chatFragmentView.updateMessageState(textMessage, position);
                    }

                    @Override
                    public void onRequestError(String message) {
                        chatFragmentView.showFirstLoadingMessagesError();
                    }
                });
    }

    @Override
    public void unregisterOnMessageAddedListener() {
        chatFragmentInteractor.unregisterOnMessageChangedListener();
    }

    @Override
    public void registerFriendTypingListener(String ignoreUserID) {
        chatFragmentInteractor.registerFriendTypingListener(roomID, ignoreUserID, (userID, state) -> {
            if (state) {
                chatFragmentView.showTypingStateView(userID);
            } else {
                chatFragmentView.hideTypingStateView(userID);
            }
        });
    }

    @Override
    public void unregisterFriendTypingListener() {
        chatFragmentInteractor.unregisterFriendTypingListener();
    }

    @Override
    public void fetchLatestMessages() {
        chatFragmentView.clearAllMessages();
        chatFragmentView.showFirstLoadingMessagesProgress();
        chatFragmentInteractor.getMessages(roomID, lastTextMessage, Constants.PAGE_SIZE,
                new OnGetMessagesCompleteListener() {
                    @Override
                    public void onGetMessageSuccess(List<TextMessage> textMessages) {
                        chatFragmentView.addMessages(textMessages);
                    }

                    @Override
                    public void onLastElementFetched(TextMessage element, boolean hasNoElementLeft) {
                        hasNoMessageLeft = hasNoElementLeft;
                        lastTextMessage = element;
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                    }

                    @Override
                    public void onRequestError(String message) {
                        chatFragmentView.showFirstLoadingMessagesError();
                    }
                });
    }

    @Override
    public void loadMoreMessages() {
        if (hasNoMessageLeft) {
            return;
        }
        chatFragmentView.showLoadingMoreProgress();
        chatFragmentInteractor.getMessages(roomID, lastTextMessage, Constants.PAGE_SIZE,
                new OnGetMessagesCompleteListener() {
                    @Override
                    public void onGetMessageSuccess(List<TextMessage> textMessages) {
                        chatFragmentView.hideLoadingMoreProgress();
                        chatFragmentView.addMessages(textMessages);
                    }

                    @Override
                    public void onLastElementFetched(TextMessage element, boolean hasNoElementLeft) {
                        lastTextMessage = element;
                        hasNoMessageLeft = hasNoElementLeft;
                    }

                    @Override
                    public void onRequestError(String message) {
                        chatFragmentView.hideLoadingMoreProgress();
                        ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
                    }
                });
    }

    @Override
    public void validateSentTextMessage(String message) {
        if (message.isEmpty()) {
            return;
        }
        chatFragmentView.clearTypedMessage();
        chatFragmentInteractor.sendTextMessage(roomID, message, new OnRequestCompleteListener() {
            @Override
            public void onRequestSuccess() {

            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.sent_message_failed);
            }
        });
    }

    @Override
    public void validateSentImageMessage(List<Uri> imageUris) {
        if (imageUris.isEmpty()) {
            return;
        }
        Intent intent = new Intent(context, SendImageMessageService.class);
        intent.putExtra(Constants.KEY_IMAGE_FOLDER, roomID);
        ArrayList<String> imageUrisString = new ArrayList<>(imageUris.size());
        for (Uri uri : imageUris) {
            imageUrisString.add(uri.toString());
        }
        intent.putStringArrayListExtra(Constants.KEY_IMAGE_URIS, imageUrisString);
        context.startService(intent);
    }

    @Override
    public UserInfo getOwnerInfo() {
        return ownerInfo;
    }

    @Override
    public Map<String, UserInfo> getMapFriendsInfo() {
        return mapFriendsInfo;
    }

    @Override
    public void changeUserTypingState(boolean state) {
        chatFragmentInteractor.updateUserTypingState(roomID, ownerInfo.getId(), state,
                new OnRequestCompleteListener() {
                    @Override
                    public void onRequestSuccess() {
                    }

                    @Override
                    public void onRequestError(String message) {
                    }
                });
    }

    @Override
    public void registerFriendOnlineStateListener(String userID) {
        chatFragmentInteractor.registerUserStateChangeListener(userID, new OnUserOnlineStateChangeListener() {
            @Override
            public void onOnlineStateChanged(boolean isOnline) {
                chatFragmentView.updateFriendOnlineState(isOnline);
            }

            @Override
            public void onRequestError(String message) {

            }
        });
    }

    @Override
    public void unRegisterFriendOnlineStateListener() {
        chatFragmentInteractor.unRegisterUserStateChangeListener();
    }
}
