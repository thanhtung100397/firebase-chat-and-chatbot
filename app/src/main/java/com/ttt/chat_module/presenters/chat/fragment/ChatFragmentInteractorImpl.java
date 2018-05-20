package com.ttt.chat_module.presenters.chat.fragment;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.UserSettings;
import com.ttt.chat_module.models.google_map.Location;
import com.ttt.chat_module.models.message_models.EmojiMessage;
import com.ttt.chat_module.models.message_models.LocationMessage;
import com.ttt.chat_module.models.wrapper_model.LastMessageWrapper;
import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.TypingState;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentInteractorImpl implements ChatFragmentInteractor {
    private ListenerRegistration messageChangeListenerRegistration;
    private ListenerRegistration typingStateChangeListenerRegistration;
    private List<ListenerRegistration> userInfoListenerRegistrations;
    private ListenerRegistration userVisitStateChangeListenerRegistration;
    private ListenerRegistration userSettingsChangeListenerRegistration;
    private boolean lastMessageFetched = false;
    private Context context;

    public ChatFragmentInteractorImpl(Context context) {
        this.context = context;
        this.userInfoListenerRegistrations = new ArrayList<>();
    }

    @Override
    public void onViewDestroy() {
    }

    @Override
    public void sendTextMessage(String roomID, String message, OnSendMessageCompleteListener listener) {
        DocumentReference chatRoomRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID);

        TextMessage textMessage = new TextMessage(UserAuth.getUserID(), message);

        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        writeBatch.set(chatRoomRef.collection(ChatRoomInfo.MESSAGES).document(), textMessage);
        writeBatch.set(chatRoomRef, new LastMessageWrapper(textMessage), SetOptions.merge());
        writeBatch.commit()
                .addOnSuccessListener(documentReference -> listener.onSendMessageSuccess(textMessage))
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void sendEmojiImageMessage(String roomID, String type, String emojiID, OnSendMessageCompleteListener listener) {
        DocumentReference chatRoomRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID);

        EmojiMessage emojiMessage = new EmojiMessage(UserAuth.getUserID(), type, emojiID);

        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        writeBatch.set(chatRoomRef.collection(ChatRoomInfo.MESSAGES).document(), emojiMessage);
        writeBatch.set(chatRoomRef, new LastMessageWrapper(emojiMessage), SetOptions.merge());
        writeBatch.commit()
                .addOnSuccessListener(documentReference -> listener.onSendMessageSuccess(emojiMessage))
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void sendLocationMessage(String roomID, Location location, String address, OnSendMessageCompleteListener listener) {
        DocumentReference chatRoomRef = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID);

        LocationMessage locationMessage = new LocationMessage(UserAuth.getUserID(), location.getLat(), location.getLng(), address);

        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        writeBatch.set(chatRoomRef.collection(ChatRoomInfo.MESSAGES).document(), locationMessage);
        writeBatch.set(chatRoomRef, new LastMessageWrapper(locationMessage), SetOptions.merge());
        writeBatch.commit()
                .addOnSuccessListener(documentReference -> listener.onSendMessageSuccess(locationMessage))
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void registerUserVisitStateListener(String roomID, OnUserVisitStateChangeListener listener) {
        userVisitStateChangeListenerRegistration = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.VISIT_STATES)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()) {
                            case ADDED:
                            case MODIFIED: {
                                DocumentSnapshot userVisitStateSnapshot = documentChange.getDocument();
                                listener.onUserVisitStateChanged(userVisitStateSnapshot.getId(),
                                        userVisitStateSnapshot.toObject(VisitState.class));
                            }
                            break;

                            default: {
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void unregisterUserVisitStateListener() {
        if (userVisitStateChangeListenerRegistration != null) {
            userVisitStateChangeListenerRegistration.remove();
        }
    }

    @Override
    public void registerOnMessageChangedListener(String roomID,
                                                 int pageSize,
                                                 OnMessageChangedListener listener) {
        Query messagesQuery = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.MESSAGES)
                .orderBy(BaseMessage.CREATED_DATE, Query.Direction.DESCENDING)
                .limit(pageSize);

        messageChangeListenerRegistration = messagesQuery
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    int size = documentChanges.size();
                    if (!lastMessageFetched) {
                        TextMessage lastTextMessage = size == 0 ?
                                null : documentChanges.get(0).getDocument().toObject(TextMessage.class);
                        listener.onLastElementFetched(lastTextMessage,
                                lastTextMessage == null || size < pageSize);
                        lastMessageFetched = true;
                    }
                    for (int i = size - 1; i >= 0; i--) {
                        resolveMessageType(documentChanges.get(i), listener);
                    }
                });
    }

    private void resolveMessageType(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getDocument().getString(BaseMessage.TYPE)) {
            case BaseMessage.TEXT_MESSAGE: {
                resolveTextMessage(documentChange, listener);
            }
            break;

            case BaseMessage.IMAGE_MESSAGE: {
                resolveImageMessage(documentChange, listener);
            }
            break;

            case BaseMessage.EMOJI_MESSAGE: {
                resolveEmojiMessage(documentChange, listener);
            }
            break;

            case BaseMessage.LOCATION_MESSAGE: {
                resolveLocationMessage(documentChange, listener);
            }
            break;

            default: {
                break;
            }
        }
    }

    private void resolveTextMessage(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getType()) {
            case ADDED: {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                listener.onMessageAdded(documentSnapshot.getId(), documentSnapshot.toObject(TextMessage.class));
            }
            break;

            case MODIFIED: {
                listener.onMessageModified(documentChange.getDocument().toObject(TextMessage.class),
                        documentChange.getOldIndex());
            }
            break;

            default: {
                break;
            }
        }
    }

    private void resolveImageMessage(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getType()) {
            case ADDED: {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                if (!documentSnapshot.getMetadata().hasPendingWrites()) {
                    listener.onMessageAdded(documentSnapshot.getId(), documentSnapshot.toObject(ImageMessage.class));
                }
            }
            break;

            case MODIFIED: {
                listener.onMessageModified(documentChange.getDocument().toObject(ImageMessage.class),
                        documentChange.getOldIndex());
            }
            break;

            default: {
                break;
            }
        }
    }

    private void resolveEmojiMessage(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getType()) {
            case ADDED: {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                listener.onMessageAdded(documentSnapshot.getId(), documentSnapshot.toObject(EmojiMessage.class));
            }
            break;

            case MODIFIED: {
                listener.onMessageModified(documentChange.getDocument().toObject(EmojiMessage.class),
                        documentChange.getOldIndex());
            }
            break;

            default: {
                break;
            }
        }
    }

    private void resolveLocationMessage(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getType()) {
            case ADDED: {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                listener.onMessageAdded(documentSnapshot.getId(), documentSnapshot.toObject(LocationMessage.class));
            }
            break;

            case MODIFIED: {
                listener.onMessageModified(documentChange.getDocument().toObject(LocationMessage.class), documentChange.getOldIndex());
            }
            break;

            default: {
                break;
            }
        }
    }

    public void unregisterOnMessageChangedListener() {
        if (messageChangeListenerRegistration != null) {
            messageChangeListenerRegistration.remove();
        }
    }

    @Override
    public void registerFriendTypingListener(String roomID, String ignoreUserID, OnTypingStateChangeListener listener) {
        typingStateChangeListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.TYPING_STATES)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()) {
                            case ADDED: {
                                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                                String userID = documentSnapshot.getId();
                                if (userID.equals(ignoreUserID)) {
                                    return;
                                }
                                listener.onTypingStateChanged(userID, documentSnapshot.getBoolean(TypingState.IS_TYPING));
                            }
                            break;

                            case MODIFIED: {
                                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                                String userID = documentSnapshot.getId();
                                if (userID.equals(ignoreUserID)) {
                                    return;
                                }
                                listener.onTypingStateChanged(userID, documentSnapshot.getBoolean(TypingState.IS_TYPING));
                            }
                            break;

                            default: {
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void unregisterFriendTypingListener() {
        if (typingStateChangeListenerRegistration != null) {
            typingStateChangeListenerRegistration.remove();
        }
    }

    @Override
    public void updateUserTypingState(String roomID, String userID, boolean state,
                                      OnRequestCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.TYPING_STATES)
                .document(userID)
                .update(TypingState.IS_TYPING, state)
                .addOnSuccessListener(aVoid -> listener.onRequestSuccess())
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void getMessages(String roomID,
                            BaseMessage lastMessage, int pageSize,
                            OnGetMessagesCompleteListener listener) {
        Query messageQuery = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.MESSAGES)
                .orderBy(BaseMessage.CREATED_DATE, Query.Direction.DESCENDING)
                .limit(pageSize);
        if (lastMessage != null) {
            messageQuery = messageQuery.startAfter(lastMessage.getCreatedDate());
        }

        messageQuery.get().addOnSuccessListener(documentSnapshots -> {
            List<DocumentSnapshot> documentSnapshotList = documentSnapshots.getDocuments();
            List<BaseMessage> baseMessages = new ArrayList<>(documentSnapshotList.size());
            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                switch (documentSnapshot.getString(BaseMessage.TYPE)) {
                    case BaseMessage.TEXT_MESSAGE: {
                        baseMessages.add(documentSnapshot.toObject(TextMessage.class));
                    }
                    break;

                    case BaseMessage.IMAGE_MESSAGE: {
                        baseMessages.add(documentSnapshot.toObject(ImageMessage.class));
                    }
                    break;

                    case BaseMessage.EMOJI_MESSAGE: {
                        baseMessages.add(documentSnapshot.toObject(EmojiMessage.class));
                    }
                    break;

                    case BaseMessage.LOCATION_MESSAGE: {
                        baseMessages.add(documentSnapshot.toObject(LocationMessage.class));
                    }
                    break;

                    default: {
                        break;
                    }
                }
            }
            listener.onGetMessageSuccess(baseMessages);
            BaseMessage lastElement = lastMessage;
            int messageCount = baseMessages.size();
            if (messageCount > 0) {
                lastElement = baseMessages.get(messageCount - 1);
            }
            listener.onLastElementFetched(lastElement, messageCount < pageSize);
        }).addOnFailureListener(e -> {
            listener.onRequestError(e.getMessage());
        });
    }

    @Override
    public void registerUserStateChangeListener(Map<String, UserInfo> usersInfoMap, OnUserChangeListener listener) {
        for (Map.Entry<String, UserInfo> entry : usersInfoMap.entrySet()) {
            ListenerRegistration userInfoListenerRegistration = FirebaseFirestore.getInstance()
                    .collection(Constants.USERS_COLLECTION)
                    .document(entry.getKey())
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            listener.onRequestError(e.getMessage());
                            return;
                        }
                        User user = documentSnapshot.toObject(User.class);
                        listener.onUserChanged(user);
                    });
            userInfoListenerRegistrations.add(userInfoListenerRegistration);
        }
    }

    @Override
    public void unRegisterUserStateChangeListener() {
        int size = userInfoListenerRegistrations.size();
        for (int i = size - 1; i >= 0; i--) {
            userInfoListenerRegistrations.remove(i).remove();
        }
    }

    @Override
    public void registerUserSettingsChangeListener(String roomID, OnUserSettingsChangeListener listener) {
        userSettingsChangeListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.USER_SETTINGS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()) {
                            case ADDED:
                            case MODIFIED: {
                                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                                listener.onUserSettingsChange(documentSnapshot.getId(),
                                        documentSnapshot.toObject(UserSettings.class));
                            }

                            default: {
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void unregisterUserSettingChangeListener() {
        if (userSettingsChangeListenerRegistration != null) {
            userSettingsChangeListenerRegistration.remove();
        }
    }

    @Override
    public void enterRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.VISIT_STATES)
                .document(userID)
                .set(new VisitState(VisitState.IN_ROOM_STATE))
                .addOnSuccessListener(aVoid -> listener.onChangeInRoomStateSuccess(true))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void leftRoom(String roomID, String userID, OnChangeInRoomStateCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.VISIT_STATES)
                .document(userID)
                .set(new VisitState(VisitState.LEFT_ROOM_STATE))
                .addOnSuccessListener(aVoid -> listener.onChangeInRoomStateSuccess(false))
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void updateUserSettings(String roomID, String userID, boolean enableNotification,
                                   OnRequestCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(ChatRoomInfo.USER_SETTINGS)
                .document(userID)
                .set(new UserSettings(enableNotification))
                .addOnSuccessListener(aVoid -> listener.onRequestSuccess())
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }
}
