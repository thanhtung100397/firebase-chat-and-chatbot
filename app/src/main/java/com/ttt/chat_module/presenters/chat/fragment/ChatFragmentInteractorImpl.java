package com.ttt.chat_module.presenters.chat.fragment;

import android.content.Context;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.Message;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class ChatFragmentInteractorImpl implements ChatFragmentInteractor {
    private ListenerRegistration messageChangeListenerRegistration;

    @Override
    public void onViewDestroy() {
    }

    @Override
    public void sendMessage(String roomID, String message, OnRequestCompleteListener listener) {
        Message objectMessage = new Message(UserAuth.getUserID(), message);
        Map<String, Message> messageMap = new HashMap<>();
        messageMap.put(Constants.MESSAGES_COLLECTIONS, objectMessage);
        FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .set(messageMap, SetOptions.merge())
                .addOnSuccessListener(success -> listener.onRequestSuccess())
                .addOnFailureListener(error -> listener.onRequestError(error.getMessage()));
    }

    @Override
    public void registerOnMessageChangedListener(String roomID, OnMessageChangedListener listener) {
        messageChangeListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                        switch (documentChange.getType()) {
                            case ADDED: {
                                listener.onMessageAdded(documentChange.getDocument().toObject(Message.class));
                            }
                            break;

                            case MODIFIED: {
                                listener.onMessageModified(documentChange.getDocument().toObject(Message.class));
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
        messageChangeListenerRegistration.remove();
    }

    @Override
    public void getMessages(String roomID,
                            DocumentSnapshot startAtDocumentSnapshot,
                            int pageSize,
                            OnEachMessagesFetchedListener listener) {
        Query getMessageQuery = FirebaseFirestore.getInstance().collection(Constants.CHAT_ROOMS_COLLECTION)
                .document(roomID)
                .collection(Constants.MESSAGES_COLLECTIONS)
                .orderBy(Message.CREATED_DATE, Query.Direction.DESCENDING)
                .limit(pageSize);
        if (startAtDocumentSnapshot != null) {
            getMessageQuery.startAt(startAtDocumentSnapshot);
        }
        getMessageQuery.get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<DocumentSnapshot> documentSn = documentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : documentSn) {
                        listener.onEachMessageFetched(documentSnapshot.toObject(Message.class));
                    }
                    listener.onFetchMessagesCompleted();
                    int size = documentSn.size();
                    if(size > 0) {
                        listener.onLastDocumentSnapshotFetched(documentSn.get(documentSn.size() - 1));
                    }
                })
                .addOnFailureListener(e -> listener.onServerError(e.getMessage()));
    }
}
