package com.ttt.chat_module.presenters.chat.fragment;

import android.content.Context;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.notification.NewMessageNotification;
import com.ttt.chat_module.models.notification.TopicNotification;
import com.ttt.chat_module.models.wrapper_model.LastMessageWrapper;
import com.ttt.chat_module.models.VisitState;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.ImageMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.TypingState;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;
import com.ttt.chat_module.services.google_api.GoogleApiClient;
import com.ttt.chat_module.services.google_api.firebase_topic_notification.FirebaseTopicNotificationService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentInteractorImpl implements ChatFragmentInteractor {
    private ListenerRegistration messageChangeListenerRegistration;
    private ListenerRegistration typingStateChangeListenerRegistration;
    private ListenerRegistration userStateChangeListenerRegistration;
    private ListenerRegistration userVisitStateChangeListenerRegistration;
    private boolean lastMessageFetched = false;
    private Context context;

    public ChatFragmentInteractorImpl(Context context) {
        this.context = context;
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
                                null : documentChanges.get(size - 1).getDocument().toObject(TextMessage.class);
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

            default: {
                break;
            }
        }
    }

    private void resolveTextMessage(DocumentChange documentChange, OnMessageChangedListener listener) {
        switch (documentChange.getType()) {
            case ADDED: {
                listener.onMessageAdded(documentChange.getDocument().toObject(TextMessage.class));
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
                    listener.onMessageAdded(documentSnapshot.toObject(ImageMessage.class));
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
    public void registerUserStateChangeListener(String userID, OnUserOnlineStateChangeListener listener) {
        userStateChangeListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(userID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                    }
                    listener.onOnlineStateChanged(documentSnapshot.getBoolean(User.IS_ONLINE));
                });
    }

    @Override
    public void unRegisterUserStateChangeListener() {
        if (userStateChangeListenerRegistration != null) {
            userStateChangeListenerRegistration.remove();
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
}
