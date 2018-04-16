package com.ttt.chat_module.presenters.chat.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.notification.NewMessageNotification;
import com.ttt.chat_module.models.notification.NewTextMessageNotification;
import com.ttt.chat_module.models.notification.TopicNotification;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.services.SendImageMessageService;
import com.ttt.chat_module.services.SendNotificationService;
import com.ttt.chat_module.views.chat.fragment.ChatFragmentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentPresenterImpl implements ChatFragmentPresenter {
    public static final String TAG = "ChatFragPreImpl";

    private Context context;
    private ChatFragmentView chatFragmentView;
    private ChatFragmentInteractor chatFragmentInteractor;

    private String roomID;
    private UserInfo ownerInfo;
    private boolean hasNoMessageLeft;
    private Map<String, UserInfo> mapFriendsInfo;
    private Map<String, VisitState> mapFriendVisitState;
    private boolean hasUploadingTask = false;
    private boolean isInRoom = false;
    private ServiceConnection notificationServiceConnection;
    private SendNotificationService sendNotificationServiceInstance;

    private BaseMessage lastMessage = null;

    public ChatFragmentPresenterImpl(Context context,
                                     ChatFragmentView chatFragmentView,
                                     String ownerID,
                                     ChatRoomInfo chatRoomInfo) {
        this.context = context;
        this.chatFragmentView = chatFragmentView;
        this.chatFragmentInteractor = new ChatFragmentInteractorImpl(context);

        this.roomID = chatRoomInfo.getId();
        Map<String, UserInfo> usersInfo = chatRoomInfo.getUsersInfo();
        this.ownerInfo = usersInfo.remove(ownerID);
        this.mapFriendsInfo = usersInfo;
        this.mapFriendVisitState = new HashMap<>(mapFriendsInfo.size());
    }

    public boolean isCoupleChatRoom() {
        return mapFriendsInfo.size() == 1;
    }

    @Override
    public String getRoomID() {
        return roomID;
    }

    @Override
    public void onViewDestroy() {
        chatFragmentInteractor.onViewDestroy();
    }

    @Override
    public void bindNotificationService() {
        notificationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                sendNotificationServiceInstance = ((SendNotificationService.SendNotificationBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                sendNotificationServiceInstance = null;
            }
        };
        Intent intent = new Intent(context, SendNotificationService.class);
        context.bindService(intent, notificationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindNotificationService() {
        context.unbindService(notificationServiceConnection);
    }

    private void sendTextMessageNotification(UserInfo userInfo, BaseMessage baseMessage) {
        if (sendNotificationServiceInstance != null) {
            NewMessageNotification notificationPayload;
            switch (baseMessage.getType()){
                case BaseMessage.TEXT_MESSAGE:{
                    notificationPayload = new NewTextMessageNotification(roomID, userInfo, (TextMessage) baseMessage);
                }
                break;

                default:{
                    notificationPayload = null;
                    break;
                }
            }
            if(notificationPayload != null) {
                TopicNotification<NewMessageNotification> newMessageNotification =
                        new TopicNotification<>(userInfo.getId(), notificationPayload);
                sendNotificationServiceInstance.sendTopicNotification(newMessageNotification);
            }
        }
    }

    @Override
    public void registerOnFriendVisitStageListener() {
        chatFragmentInteractor.registerUserVisitStateListener(roomID, new OnUserVisitStateChangeListener() {
            @Override
            public void onUserVisitStateChanged(String userID, VisitState visitState) {
                if (!ownerInfo.getId().equals(userID)) {
                    mapFriendVisitState.put(userID, visitState);
                }
            }

            @Override
            public void onRequestError(String message) {

            }
        });
    }

    @Override
    public void unregisterOnFriendVisitStageListener() {
        chatFragmentInteractor.unregisterUserVisitStateListener();
    }

    @Override
    public void registerOnMessageAddedListener() {
        chatFragmentView.showFirstLoadingMessagesProgress();
        chatFragmentInteractor.registerOnMessageChangedListener(roomID, Constants.PAGE_SIZE,
                new OnMessageChangedListener() {
                    @Override
                    public void onLastElementFetched(TextMessage element, boolean hasNoElementLeft) {
                        hasNoMessageLeft = hasNoElementLeft;
                        lastMessage = element;
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                    }

                    @Override
                    public void onMessageAdded(BaseMessage baseMessage) {
                        chatFragmentView.addTopMessage(baseMessage);
                    }

                    @Override
                    public void onMessageModified(BaseMessage baseMessage, int position) {
                        chatFragmentView.updateMessageState(baseMessage, position);
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
        chatFragmentInteractor.getMessages(roomID, lastMessage, Constants.PAGE_SIZE,
                new OnGetMessagesCompleteListener() {
                    @Override
                    public void onGetMessageSuccess(List<BaseMessage> baseMessages) {
                        chatFragmentView.addMessages(baseMessages);
                    }

                    @Override
                    public void onLastElementFetched(BaseMessage element, boolean hasNoElementLeft) {
                        hasNoMessageLeft = hasNoElementLeft;
                        lastMessage = element;
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
        chatFragmentInteractor.getMessages(roomID, lastMessage, Constants.PAGE_SIZE,
                new OnGetMessagesCompleteListener() {
                    @Override
                    public void onGetMessageSuccess(List<BaseMessage> baseMessages) {
                        chatFragmentView.hideLoadingMoreProgress();
                        chatFragmentView.addMessages(baseMessages);
                    }

                    @Override
                    public void onLastElementFetched(BaseMessage element, boolean hasNoElementLeft) {
                        lastMessage = element;
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
        if (hasUploadingTask) {
            ToastUtils.quickToast(context, R.string.uploading_task_inprogress);
            return;
        }
        chatFragmentView.clearTypedMessage();
        chatFragmentInteractor.sendTextMessage(roomID, message, new OnSendMessageCompleteListener() {
            @Override
            public void onSendMessageSuccess(BaseMessage message) {
                for (Map.Entry<String, VisitState> visitStateEntry : mapFriendVisitState.entrySet()) {
                    sendTextMessageNotification(ownerInfo, message);
//                    if (visitStateEntry.getValue().getState().equals(VisitState.LEFT_ROOM_STATE)) {
//                        sendTextMessageNotification(mapFriendsInfo.get(visitStateEntry.getKey()), message);
//                    }
                }
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
        intent.putExtra(Constants.KEY_OWNER_ID, ownerInfo.getId());
        ArrayList<String> imageUrisString = new ArrayList<>(imageUris.size());
        for (Uri uri : imageUris) {
            imageUrisString.add(uri.toString());
        }
        intent.putStringArrayListExtra(Constants.KEY_IMAGE_URIS, imageUrisString);
        context.startService(intent);
        ImageMessage imageMessage = new ImageMessage(ownerInfo.getId(), imageUris);
        chatFragmentView.addTopMessage(imageMessage);
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

    @Override
    public void setHasUploadingTask(boolean hasUploadingTask) {
        this.hasUploadingTask = hasUploadingTask;
    }

    @Override
    public void enterRoom() {
        chatFragmentInteractor.enterRoom(roomID, ownerInfo.getId(), new OnChangeInRoomStateCompleteListener() {
            @Override
            public void onChangeInRoomStateSuccess(boolean inRoomState) {
                isInRoom = inRoomState;
            }

            @Override
            public void onRequestError(String message) {
                Log.i(TAG, "onRequestError: " + message);
            }
        });
    }

    @Override
    public void leftRoom() {
        chatFragmentInteractor.leftRoom(roomID, ownerInfo.getId(), new OnChangeInRoomStateCompleteListener() {
            @Override
            public void onChangeInRoomStateSuccess(boolean inRoomState) {
                isInRoom = inRoomState;
            }

            @Override
            public void onRequestError(String message) {
                Log.i(TAG, "onRequestError: " + message);
            }
        });
    }

    @Override
    public boolean isUserInChatRoom() {
        return isInRoom;
    }
}
