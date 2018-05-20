package com.ttt.chat_module.presenters.chat.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.ttt.chat_module.R;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.ToastUtils;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserSettings;
import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.EmojiMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.LocationMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.notification.NewEmojiMessageNotification;
import com.ttt.chat_module.models.notification.NewImageMessageNotification;
import com.ttt.chat_module.models.notification.NewLocationMessageNotification;
import com.ttt.chat_module.models.notification.NewMessageNotification;
import com.ttt.chat_module.models.notification.NewTextMessageNotification;
import com.ttt.chat_module.models.notification.TopicNotification;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.services.SeenMessagesService;
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
    private Map<String, UserSettings> mapUserSettings;
    private Map<String, VisitState> mapFriendVisitState;
    private boolean hasUploadingTask = false;
    private boolean isInRoom = false;
    private ServiceConnection notificationServiceConnection;
    private SendNotificationService sendNotificationServiceInstance;
    private ServiceConnection seenMessagesServiceConnection;
    private SeenMessagesService seenMessagesServiceInstance;

    private List<String> unseenMessageIDs;
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
        this.mapUserSettings = new HashMap<>(mapFriendsInfo.size());
        this.mapFriendVisitState = new HashMap<>(mapFriendsInfo.size());
        this.unseenMessageIDs = new ArrayList<>();
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
    public void registerServices() {
        bindNotificationService();
        bindSeenMessagesServiceThenRegisterOnMessageAddedListener();
        registerOnFriendVisitStageListener();
        registerFriendTypingListener(ownerInfo.getId());
        registerFriendsInfoChangeListener();
        registerUserSettingsChangeListener();
    }

    @Override
    public void unregisterServices() {
        unbindNotificationService();
        unbindSeenMessagesService();
        unregisterOnFriendVisitStageListener();
        unregisterOnMessageAddedListener();
        unregisterFriendTypingListener();
        unregisterFriendInfoChangeListener();
        unregisterUserSettingsChangeListener();
    }

    private void bindNotificationService() {
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

    private void unbindNotificationService() {
        if (notificationServiceConnection != null) {
            context.unbindService(notificationServiceConnection);
        }
    }

    private void sendMessageNotification(String toUserID, BaseMessage baseMessage) {
        if (!mapUserSettings.get(toUserID).getEnableNotification()) {
            return;
        }

        if (sendNotificationServiceInstance != null) {
            NewMessageNotification notificationPayload;
            switch (baseMessage.getType()) {
                case BaseMessage.TEXT_MESSAGE: {
                    notificationPayload = new NewTextMessageNotification(roomID, toUserID, ownerInfo, (TextMessage) baseMessage);
                }
                break;

                case BaseMessage.EMOJI_MESSAGE: {
                    notificationPayload = new NewEmojiMessageNotification(roomID, toUserID, ownerInfo, (EmojiMessage) baseMessage);
                }
                break;

                case BaseMessage.IMAGE_MESSAGE: {
                    notificationPayload = new NewImageMessageNotification(roomID, toUserID, ownerInfo, (ImageMessage) baseMessage);
                }
                break;

                case BaseMessage.LOCATION_MESSAGE: {
                    notificationPayload = new NewLocationMessageNotification(roomID, toUserID, ownerInfo, (LocationMessage) baseMessage);
                }
                break;

                default: {
                    notificationPayload = null;
                    break;
                }
            }
            if (notificationPayload != null) {
                TopicNotification<NewMessageNotification> newMessageNotification =
                        new TopicNotification<>(toUserID, notificationPayload);
                sendNotificationServiceInstance.sendTopicNotification(newMessageNotification);
            }
        }
    }

    private void registerOnFriendVisitStageListener() {
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

    private void unregisterOnFriendVisitStageListener() {
        chatFragmentInteractor.unregisterUserVisitStateListener();
    }

    private void registerOnMessageAddedListener() {
        chatFragmentView.showFirstLoadingMessagesProgress();
        chatFragmentInteractor.registerOnMessageChangedListener(roomID, Constants.PAGE_SIZE,
                new OnMessageChangedListener() {
                    @Override
                    public void onLastElementFetched(TextMessage element, boolean hasNoElementLeft) {
                        hasNoMessageLeft = hasNoElementLeft;
                        lastMessage = element;
                        chatFragmentView.hideFirstLoadingMessagesProgress();
                        seenAllUnseenMessage();
                    }

                    @Override
                    public void onMessageAdded(String id, BaseMessage baseMessage) {
                        chatFragmentView.addTopMessage(baseMessage);
                        if (!baseMessage.getOwnerID().equals(ownerInfo.getId())) {
                            if (baseMessage.getSeenBy() == null || !baseMessage.getSeenBy().containsKey(ownerInfo.getId())) {
                                unseenMessageIDs.add(id);
                            }
                        }
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

    private void unregisterOnMessageAddedListener() {
        chatFragmentInteractor.unregisterOnMessageChangedListener();
    }

    private void registerFriendTypingListener(String ignoreUserID) {
        chatFragmentInteractor.registerFriendTypingListener(roomID, ignoreUserID, (userID, state) -> {
            if (state) {
                chatFragmentView.showTypingStateView(userID);
            } else {
                chatFragmentView.hideTypingStateView(userID);
            }
        });
    }

    private void unregisterFriendTypingListener() {
        chatFragmentInteractor.unregisterFriendTypingListener();
    }

    private void registerFriendsInfoChangeListener() {
        chatFragmentInteractor.registerUserStateChangeListener(mapFriendsInfo, new OnUserChangeListener() {
            @Override
            public void onUserChanged(User user) {
                UserInfo userInfo = new UserInfo(user);
                mapFriendsInfo.put(user.getId(), userInfo);
                chatFragmentView.updateFriend(userInfo, isOnline());
            }

            @Override
            public void onRequestError(String message) {

            }
        });
    }

    private void unregisterFriendInfoChangeListener() {
        chatFragmentInteractor.unRegisterUserStateChangeListener();
    }

    private void registerUserSettingsChangeListener() {
        chatFragmentInteractor.registerUserSettingsChangeListener(roomID, new OnUserSettingsChangeListener() {
            @Override
            public void onUserSettingsChange(String userID, UserSettings userSettings) {
                mapUserSettings.put(userID, userSettings);
                chatFragmentView.updateNotificationMenuItem(userSettings.getEnableNotification());
            }

            @Override
            public void onRequestError(String message) {

            }
        });
    }

    private void unregisterUserSettingsChangeListener() {
        chatFragmentInteractor.unregisterUserSettingChangeListener();
    }

    private void bindSeenMessagesServiceThenRegisterOnMessageAddedListener() {
        seenMessagesServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                seenMessagesServiceInstance = ((SeenMessagesService.SeenMessagesServiceBinder) iBinder).getService();
                registerOnMessageAddedListener();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                seenMessagesServiceInstance = null;
            }
        };
        Intent intent = new Intent(context, SeenMessagesService.class);
        context.bindService(intent, seenMessagesServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindSeenMessagesService() {
        context.unbindService(seenMessagesServiceConnection);
        seenMessagesServiceInstance = null;
    }

    @Override
    public void seenAllUnseenMessage() {
        if (seenMessagesServiceInstance != null && !unseenMessageIDs.isEmpty()) {
            seenMessagesServiceInstance.seenAllMessages(roomID, ownerInfo.getId(),
                    unseenMessageIDs, !lastMessage.getOwnerID().equals(ownerInfo.getId()));
            unseenMessageIDs.clear();
        }
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
    public void validateEmojiImageMessage(String type, String emojiID) {
        if (hasUploadingTask) {
            ToastUtils.quickToast(context, R.string.uploading_task_in_progress);
            return;
        }
        chatFragmentInteractor.sendEmojiImageMessage(roomID, type, emojiID, new OnSendMessageCompleteListener() {
            @Override
            public void onSendMessageSuccess(BaseMessage message) {
                sendNotificationToLeftRoomFriends(message);
            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.sent_message_failed);
            }
        });
    }

    @Override
    public void validateLocationMessage(Location location, String address) {
        if (location == null || address == null) {
            return;
        }
        if (hasUploadingTask) {
            ToastUtils.quickToast(context, R.string.uploading_task_in_progress);
            return;
        }
        chatFragmentInteractor.sendLocationMessage(roomID, location, address, new OnSendMessageCompleteListener() {
            @Override
            public void onSendMessageSuccess(BaseMessage message) {
                sendNotificationToLeftRoomFriends(message);
            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.sent_message_failed);
            }
        });
    }

    @Override
    public void validateSentTextMessage(String message) {
        if (message.isEmpty()) {
            return;
        }
        if (hasUploadingTask) {
            ToastUtils.quickToast(context, R.string.uploading_task_in_progress);
            return;
        }
        chatFragmentView.clearTypedMessage();
        chatFragmentInteractor.sendTextMessage(roomID, message, new OnSendMessageCompleteListener() {
            @Override
            public void onSendMessageSuccess(BaseMessage message) {
                sendNotificationToLeftRoomFriends(message);
            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.sent_message_failed);
            }
        });
    }

    private void sendNotificationToLeftRoomFriends(BaseMessage message) {
        for (Map.Entry<String, VisitState> visitStateEntry : mapFriendVisitState.entrySet()) {
            if (visitStateEntry.getValue().getState().equals(VisitState.LEFT_ROOM_STATE)) {
                sendMessageNotification(visitStateEntry.getKey(), message);
            }
        }
    }

    @Override
    public void validateSentImageMessage(List<Uri> imageUris) {
        if (imageUris.isEmpty()) {
            return;
        }
        if (hasUploadingTask) {
            ToastUtils.quickToast(context, R.string.uploading_task_in_progress);
            return;
        }
        hasUploadingTask = true;
        Intent intent = new Intent(context, SendImageMessageService.class);
        intent.putExtra(Constants.KEY_IMAGE_FOLDER, roomID);
        intent.putExtra(Constants.KEY_OWNER, (Parcelable) ownerInfo);
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
    public void onSendImageComplete(ImageMessage imageMessage, boolean isSuccess) {
        this.hasUploadingTask = false;
        if (isSuccess) {
            sendNotificationToLeftRoomFriends(imageMessage);
        }
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

    @Override
    public boolean isOnline() {
        if (mapFriendsInfo == null) {
            return false;
        }
        for (Map.Entry<String, UserInfo> entry : mapFriendsInfo.entrySet()) {
            if (entry.getValue().getIsOnline()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeUserNotificationFlag() {
        String ownedID = ownerInfo.getId();
        UserSettings userSettings = mapUserSettings.get(ownedID);
        boolean notificationFlag = !userSettings.getEnableNotification();
        chatFragmentInteractor.updateUserSettings(roomID, ownedID, notificationFlag, new OnRequestCompleteListener() {
            @Override
            public void onRequestSuccess() {
                if(notificationFlag) {
                    ToastUtils.quickToast(context, R.string.notification_turned_on);
                } else {
                    ToastUtils.quickToast(context, R.string.notification_turned_off);
                }
            }

            @Override
            public void onRequestError(String message) {
                ToastUtils.quickToast(context, R.string.unexpected_error_occurred);
            }
        });
    }
}
