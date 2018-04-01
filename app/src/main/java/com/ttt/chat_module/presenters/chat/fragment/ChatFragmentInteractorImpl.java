package com.ttt.chat_module.presenters.chat.fragment;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.message_models.BaseMessage;
import com.ttt.chat_module.models.message_models.TextMessage;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.TypingState;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentInteractorImpl implements ChatFragmentInteractor {
    private ListenerRegistration messageChangeListenerRegistration;
    private ListenerRegistration typingStateChangeListenerRegistration;
    private ListenerRegistration userStateChangeListenerRegistration;
    private boolean lastMessageFetched = false;

    @Override
    public void onViewDestroy() {
    }

    @Override
    public void sendTextMessage(String roomID, String message, OnRequestCompleteListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .add(new TextMessage(UserAuth.getUserID(), message))
                .addOnSuccessListener(documentReference -> listener.onRequestSuccess())
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void registerOnMessageChangedListener(String roomID,
                                                 int pageSize,
                                                 OnMessageChangedListener listener) {
        Query messagesQuery = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
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
                        DocumentChange documentChange = documentChanges.get(i);
                        TextMessage textMessage = documentChange.getDocument().toObject(TextMessage.class);
                        switch (documentChange.getType()) {
                            case ADDED: {
                                listener.onMessageAdded(textMessage);
                            }
                            break;

                            case MODIFIED: {
                                listener.onMessageModified(textMessage, documentChange.getOldIndex());
                            }
                            break;

                            default: {
                                break;
                            }
                        }
                    }
                });
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
                            TextMessage lastTextMessage, int pageSize,
                            OnGetMessagesCompleteListener listener) {
        Query messageQuery = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .orderBy(BaseMessage.CREATED_DATE, Query.Direction.DESCENDING)
                .limit(pageSize);
        if (lastTextMessage != null) {
            messageQuery = messageQuery.startAfter(lastTextMessage.getCreatedDate());
        }

        messageQuery.get().addOnSuccessListener(documentSnapshots -> {
            List<DocumentSnapshot> documentSnapshotList = documentSnapshots.getDocuments();
            List<TextMessage> textMessages = new ArrayList<>(documentSnapshotList.size());
            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                textMessages.add(documentSnapshot.toObject(TextMessage.class));
            }
            listener.onGetMessageSuccess(textMessages);
            TextMessage lastElement = lastTextMessage;
            int messageCount = textMessages.size();
            if (messageCount > 0) {
                lastElement = textMessages.get(messageCount - 1);
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
}
